package ldf.java_cup.runtime;

import java.util.Enumeration;
import java.util.NoSuchElementException;

/**
 * @author Cristian Harja
 */
public class CommentEnumeration implements Enumeration<Comment> {

    private Comment current;

    public CommentEnumeration(Comment start) {
        current = start;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasMoreElements() {
        return current != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Comment nextElement() {
        Comment ret = current;
        if (ret == null) {
            throw new NoSuchElementException();
        }
        current = ret.prevComment;
        return ret;
    }
}
