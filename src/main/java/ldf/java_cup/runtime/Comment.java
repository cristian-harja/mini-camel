package ldf.java_cup.runtime;

/**
 * @author Cristian Harja
 */
public class Comment extends LocationAwareEntityImpl {

    protected Comment prevComment;

    protected Comment() {}

    public Comment(Comment prevComment) {
        this.prevComment = prevComment;
    }

    public Comment getPrevComment() {
        return prevComment;
    }

}
