package mini_camel.ir;

import mini_camel.Pair;
import mini_camel.ir.instr.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

import static mini_camel.ir.instr.Instr.Type.*;

@SuppressWarnings("UnusedDeclaration")
public final class ControlFlowGraph {

    private static final BasicBlock[] EMPTY_PGM = new BasicBlock[]{
            new BasicBlock(1, 0, 0, Collections.<Instr>emptyList())
    };

    /**
     * Slices a sequence of instructions into a list of basic blocks. The ID
     * of each basic block is equal to its index plus one. The returned array
     * represents the control flow graph of the input IR code.
     */
    @Nonnull
    public static BasicBlock[] sliceFromInstrList(@Nonnull List<Instr> code) {
        int i, j, codeSize = code.size(), actualSize;
        Instr instr;

        // All instructions, no labels
        List<Instr> actualCode = new ArrayList<>(codeSize);

        // All labels, pointing to the first instruction
        Map<String, Integer> labels = new HashMap<>();

        // STEP 1: identify all labels, and assign a new index to each
        // instruction, by leaving out the labels
        for (i = 0, j = 0; i < codeSize; ++i) {
            instr = code.get(i);
            while (instr.getInstrType() == LABEL) {
                Label l = (Label) instr;
                labels.put(l.name, j);
                ++i;
                if (i == codeSize) break;
                instr = code.get(i);
                continue;
            }
            if (i == codeSize) break;
            actualCode.add(j++, instr);
        }
        if (actualCode.isEmpty()) {
            return EMPTY_PGM;
        }
        actualCode = Collections.unmodifiableList(actualCode);

        // STEP 2: Compute head/tail of each basic block
        actualSize = actualCode.size();
        int[] heads = new int[actualSize];
        int[] tails = new int[actualSize];

        heads[0] = 1;
        tails[actualSize - 1] = 1;

        for (i = 0; i < actualSize; ++i) {
            Pair<Label, Label> jumps = getBranches(actualCode.get(i));
            if (jumps == null) continue;
            int head1 = labels.get(jumps.left.name);
            int head2 = labels.get(jumps.right.name);

            tails[i] = 1;
            if (head1 < actualSize) heads[head1] = 1;
            if (head2 < actualSize) heads[head2] = 1;

            if (i < actualSize - 1) heads[i + 1] = 1;
            if (head1 > 0) tails[head1 - 1] = 1;
            if (head2 > 0) tails[head2 - 1] = 1;

        }

        // STEP 3: delimit the basic blocks
        int numBlocks;
        for (i = 0, numBlocks = 0; i < actualSize; ++i) {
            numBlocks += heads[i];
        }

        int[] blocksHeads = new int[numBlocks];
        int[] blocksTails = new int[numBlocks];
        int[] blocksBackRef = new int[actualSize + 1];
        int lastHead = 0;
        int lastTail = 0;
        for (i = 0; i < actualSize; ++i) {
            if (heads[i] != 0) {
                blocksHeads[lastHead++] = i;
            }
            if (tails[i] != 0) {
                blocksTails[lastTail++] = i;
            }
            blocksBackRef[i] = lastHead;
        }
        blocksBackRef[actualSize] = 0;

        // STEP 4: instantiate the basic blocks
        BasicBlock[] blocks = new BasicBlock[numBlocks];
        for (i = 0; i < numBlocks; ++i) {
            List<Instr> body = actualCode.subList(
                    blocksHeads[i],
                    blocksTails[i] + 1
            );
            Instr tail = body.get(body.size() - 1);
            Pair<Label, Label> jumps = getBranches(tail);
            int branchIfTrue, branchIfFalse;

            if (jumps != null) {
                Label left = jumps.left;
                Label right = jumps.right;
                branchIfTrue = blocksBackRef[labels.get(left.name)];
                if (left == right) {
                    body = actualCode.subList(
                            blocksHeads[i],
                            blocksTails[i]
                    );
                    branchIfFalse = branchIfTrue;
                } else {
                    branchIfFalse = blocksBackRef[labels.get(right.name)];
                }
            } else {
                int tailPos = blocksTails[i];
                if (tailPos + 1 == actualSize) {
                    branchIfTrue = 0;
                } else {
                    branchIfTrue = blocksBackRef[tailPos + 1];
                }
                branchIfFalse = branchIfTrue;
            }
            blocks[i] = new BasicBlock(
                    i + 1, branchIfTrue, branchIfFalse, body
            );
        }

        return blocks;
    }

    @Nullable
    private static Pair<Label, Label> getBranches(Instr i) {
        Instr.Type t = i.getInstrType();
        if (t == BRANCH) {
            Branch b = (Branch) i;
            return new Pair<>(b.ifTrue, b.ifFalse);
        } else if (t == JUMP) {
            Label moon = ((Jump) i).label;
            return new Pair<>(moon, moon);
        } else {
            return null;
        }
    }

}
