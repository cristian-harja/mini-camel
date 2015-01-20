package mini_camel.ir;

import mini_camel.ir.instr.Instr;

import javax.annotation.Nonnull;
import java.util.List;

public final class BasicBlock {

    /**
     * The ID of the basic block, with respect to its control-flow graph.
     */
    public final int blockId;

    /**
     * The list of instructions contained in this basic block.
     */
    public final List<Instr> body;

    /**
     * The ID of the block to which to branch if the final instruction in
     * the block is a branch and the condition evaluates to `true`. If the
     * final instruction is not a branch, this value equals `branchIfFalse`.
     * If both are zero, this is a final block.
     */
    public final int branchIfTrue;

    /**
     * The ID of the block to which to branch if the final instruction in
     * the block is a branch and the condition evaluates to `false`. If the
     * final instruction is not a branch, this value equals `branchIfTrue`.
     * If both are zero, this is a final block.
     */
    public final int branchIfFalse;

    public BasicBlock(
            int id, int ifTrue, int ifFalse,
            @Nonnull List<Instr> body
    ) {
        blockId = id;
        branchIfTrue = ifTrue;
        branchIfFalse = ifFalse;
        this.body = body;
    }
}
