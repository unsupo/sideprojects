import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;

public class PegGameTest {
    @Test
    public void firstMoveTest(){
        List<Pair<Long,Long>> p = new PegGame().getAllMoves();
        List<Pair<Long,Long>> sb = Arrays.asList(
                new Pair<Long,Long>(16777216L, 1099511627776L),
                new Pair<Long,Long>(1048576L, 1099511627776L)
        );
        assertEquals(sb,p);
    }
    @Test
    public void secondMoveTest(){
        PegGame pg = new PegGame();
        Pair<Long,Long> v = pg.getAllMoves().get(0);
        pg.jumpPeg(v.start,v.end);
        List<Pair<Long,Long>> p = pg.getAllMoves();
        List<Pair<Long,Long>> sb = Arrays.asList(
                new Pair<Long,Long>(1048576L, 16777216L),
                new Pair<Long,Long>(4096L, 4294967296L),
                new Pair<Long,Long>(256L, 16777216L),
                new Pair<Long,Long>(16L, 16777216L)
        );
        assertEquals(sb,p);
        /*
        System.out.println("List<Pair<Long,Long>> sb = Arrays.asList(");
        final List<Pair<Long,Long>> m = moves;
        moves.forEach(a->{
            System.out.printf("\tnew Pair<Long,Long>(%sL, %sL)%s\n",a.start,a.end,a.equals(m.get(m.size()-1))?"":",");
        });
        System.out.println(");");
         */
    }
    @Test
    public void thirdMoveTest(){
        PegGame pg = new PegGame();
        Pair<Long,Long> v = pg.getAllMoves().get(0);
        pg.jumpPeg(v.start,v.end);
        v = pg.getAllMoves().get(0);
        pg.jumpPeg(v.start,v.end);
        List<Pair<Long,Long>> p = pg.getAllMoves();
        List<Pair<Long,Long>> sb = Arrays.asList(
                new Pair<Long,Long>(1099511627776L, 1048576L),
                new Pair<Long,Long>(65536L, 4294967296L),
                new Pair<Long,Long>(64L, 4194304L),
                new Pair<Long,Long>(16L, 1048576L),
                new Pair<Long,Long>(4L, 4194304L),
                new Pair<Long,Long>(1L, 1048576L)
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
        List<Pair<Long,Long>> p = pg.getAllMoves();
        List<Pair<Long,Long>> sb = Arrays.asList(
                new Pair<Long,Long>(16384L, 1024L),
                new Pair<Long,Long>(4096L, 65536L),
                new Pair<Long,Long>(64L, 4194304L),
                new Pair<Long,Long>(16L, 16777216L),
                new Pair<Long,Long>(16L, 1048576L),
                new Pair<Long,Long>(4L, 4194304L)
        );
        assertEquals(sb,p);
    }

    @Test
    public void invalidJump2Test(){
        PegGame pg = new PegGame(1073807445);
        List<Pair<Long,Long>> p = pg.getAllMoves();
        List<Pair<Long,Long>> sb = Arrays.asList(
                new Pair<Long,Long>(16L, 256L)
        );
        assertEquals(sb,p);
    }

    @Test
    public void notJumpingTest(){
        PegGame pg = new PegGame(1090519045L);
        List<Pair<Long,Long>> p = pg.getAllMoves();
        List<Pair<Long,Long>> sb = Arrays.asList(
                new Pair<Long,Long>(1L, 16L)
        );
        assertEquals(sb,p);
    }
    @Test
    public void allowedMovesGreaterThanZeroTest(){
        PegGame pg = new PegGame(17830225L);
        List<Pair<Long,Long>> p = pg.getAllMoves();
        List<Pair<Long,Long>> sb = Arrays.asList(
                new Pair<Long,Long>(64L, 4L)
        );
        assertEquals(sb,p);
    }
    @Test
    public void wrongMoveTest(){
        PegGame pg = new PegGame(16843780L);
        List<Pair<Long,Long>> p = pg.getAllMoves();
        List<Pair<Long,Long>> sb = Arrays.asList(
                new Pair<Long,Long>(16777216L, 256L),
                new Pair<Long,Long>(65536L, 4294967296L)
        );
        assertEquals(sb,p);
    }
    @Test
    public void wrongMoveTest1(){
        PegGame pg = new PegGame(5385486421L);
        List<Pair<Long,Long>> p = pg.getAllMoves();
        List<Pair<Long,Long>> sb = Arrays.asList(
                new Pair<>(4294967296L, 65536L),
                new Pair<>(16777216L, 1099511627776L),
                new Pair<>(16L, 256L)
        );
        assertEquals(sb,p);
    }
    @Test
    public void illegalJumpTest(){
        PegGame pg = new PegGame(5368710485L);
        List<Pair<Long,Long>> p = pg.getAllMoves();
        List<Pair<Long,Long>> sb = Arrays.asList(
                new Pair<>(1L, 1048576L)
        );
        assertEquals(sb,p);
    }
}
