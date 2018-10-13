import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;

public class PegGameTest {
    @Test
    public void firstMoveTest(){
        List<PegGame.Pair> p = new PegGame().getAllMoves();
        List<PegGame.Pair> sb = Arrays.asList(
                new PegGame.Pair(16777216L, 1099511627776L),
                new PegGame.Pair(1048576L, 1099511627776L)
        );
        assertEquals(sb,p);
    }
    @Test
    public void secondMoveTest(){
        PegGame pg = new PegGame();
        PegGame.Pair v = pg.getAllMoves().get(0);
        pg.jumpPeg(v.start,v.end);
        List<PegGame.Pair> p = pg.getAllMoves();
        List<PegGame.Pair> sb = Arrays.asList(
                new PegGame.Pair(1048576L, 16777216L),
                new PegGame.Pair(4096L, 4294967296L),
                new PegGame.Pair(256L, 16777216L),
                new PegGame.Pair(16L, 16777216L)
        );
        assertEquals(sb,p);
        /*
        System.out.println("List<PegGame.Pair> sb = Arrays.asList(");
        final List<PegGame.Pair> m = moves;
        moves.forEach(a->{
            System.out.printf("\tnew PegGame.Pair(%sL, %sL)%s\n",a.start,a.end,a.equals(m.get(m.size()-1))?"":",");
        });
        System.out.println(");");
         */
    }
    @Test
    public void thirdMoveTest(){
        PegGame pg = new PegGame();
        PegGame.Pair v = pg.getAllMoves().get(0);
        pg.jumpPeg(v.start,v.end);
        v = pg.getAllMoves().get(0);
        pg.jumpPeg(v.start,v.end);
        List<PegGame.Pair> p = pg.getAllMoves();
        List<PegGame.Pair> sb = Arrays.asList(
                new PegGame.Pair(1099511627776L, 1048576L),
                new PegGame.Pair(65536L, 4294967296L),
                new PegGame.Pair(64L, 4194304L),
                new PegGame.Pair(16L, 1048576L),
                new PegGame.Pair(4L, 4194304L),
                new PegGame.Pair(1L, 1048576L)
        );
        assertEquals(sb,p);
    }
    @Test
    public void jumpToZerosTest(){
        long pegs = 1100602234197L,
            empties = 1099511627776L,
            outOfBounds = 34079469644458L;
        PegGame p = new PegGame(pegs);
        p.jumpPeg(1099511627776L, 1048576L);
        assertEquals(17913173L,p.getPegs());
    }

    @Test
    public void invalidJumpTest(){
        PegGame pg = new PegGame(5368729941L);
        List<PegGame.Pair> p = pg.getAllMoves();
        List<PegGame.Pair> sb = Arrays.asList(
                new PegGame.Pair(16384L, 1024L),
                new PegGame.Pair(4096L, 65536L),
                new PegGame.Pair(64L, 4194304L),
                new PegGame.Pair(16L, 16777216L),
                new PegGame.Pair(16L, 1048576L),
                new PegGame.Pair(4L, 4194304L)
        );
        assertEquals(sb,p);
    }

    @Test
    public void invalidJump2Test(){
        PegGame pg = new PegGame(1073807445);
        List<PegGame.Pair> p = pg.getAllMoves();
        List<PegGame.Pair> sb = Arrays.asList(
                new PegGame.Pair(16L, 256L)
        );
        assertEquals(sb,p);
    }

    @Test
    public void notJumpingTest(){
        PegGame pg = new PegGame(1090519045L);
        List<PegGame.Pair> p = pg.getAllMoves();
        List<PegGame.Pair> sb = Arrays.asList(
                new PegGame.Pair(1L, 16L)
        );
        assertEquals(sb,p);
    }
    @Test
    public void allowedMovesGreaterThanZeroTest(){
        PegGame pg = new PegGame(17830225L);
        List<PegGame.Pair> p = pg.getAllMoves();
        List<PegGame.Pair> sb = Arrays.asList(
                new PegGame.Pair(64L, 4L)
        );
        assertEquals(sb,p);
    }
    @Test
    public void wrongMoveTest(){
        PegGame pg = new PegGame(16843780L);
        List<PegGame.Pair> p = pg.getAllMoves();
        List<PegGame.Pair> sb = Arrays.asList(
                new PegGame.Pair(16777216L, 256L),
                new PegGame.Pair(65536L, 4294967296L)
        );
        assertEquals(sb,p);
    }
    @Test
    public void wrongMoveTest1(){
        PegGame pg = new PegGame(5385486421L);
        List<PegGame.Pair> p = pg.getAllMoves();
        List<PegGame.Pair> sb = Arrays.asList(
                new PegGame.Pair(4294967296L, 65536L),
                new PegGame.Pair(16777216L, 1099511627776L),
                new PegGame.Pair(16L, 256L)
        );
        assertEquals(sb,p);
    }
    @Test
    public void illegalJumpTest(){
        PegGame pg = new PegGame(5368710485L);
        List<PegGame.Pair> p = pg.getAllMoves();
        List<PegGame.Pair> sb = Arrays.asList(
                new PegGame.Pair(1L, 1048576L)
        );
        assertEquals(sb,p);
    }
}
