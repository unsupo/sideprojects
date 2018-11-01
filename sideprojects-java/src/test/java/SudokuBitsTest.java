import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SudokuBitsTest {
    @Test
    public void noMovesTest(){
        SudokuBits bits = new SudokuBits(new BigInteger[]{
                new BigInteger("1000000000000000000000000000000000000000000000000",2),
                new BigInteger("100000000000000000",2),
                new BigInteger("10000000000",2),
                new BigInteger("0",2),
                new BigInteger("0",2),
                new BigInteger("0",2),
                new BigInteger("0",2),
                new BigInteger("0",2),
                new BigInteger("1000",2)
        });
        assertTrue(bits.isValid(7,77));
    }

    @Test
    public void testIsSolvable(){
        SudokuBits bits = SudokuBits.solve(new SudokuBits(new int[]{
                -1, -1, 3, -1, -1, -1, 2, -1, 1,
                -1, -1, 9, 1, 3, -1, -1, -1, -1,
                -1, -1, -1, 4, -1, 8, -1, 5, -1,
                -1, 4, 8, -1, -1, -1, -1, -1, 3,
                -1, -1, -1, -1, 9, -1, -1, -1, -1,
                9, -1, -1, -1, -1, -1, 5, 6, -1,
                -1, 8, -1, 2, -1, 1, -1, -1, -1,
                -1, -1, -1, -1, 4, 5, 1, -1, -1,
                7, -1, 1, -1, -1, -1, 6, -1, -1,
        }));
        System.out.println();

    }

    @Test
    public void testOldIsValid(){
        SudokuBits sb = new SudokuBits(new int[]{
                 1, -1, -1,  2, -1, -1, -1, -1, -1,
                 0, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1,
                 5, -1, -1, -1, -1, -1, -1, -1, -1,
                -1,  1, -1, -1, -1, -1, -1, -1, -1,
                 7, -1, -1,  5, -1, -1, -1, -1,  4,
                -1, -1, -1, -1, -1, -1, -1, -1, -1,
                 9, -1, -1, -1, -1, -1, -1, -1, -1,
                 2, -1, -1, -1, -1, -1, -1,  6, -1
        });
        //same spot
        assertFalse(sb.isValidOld(1,0));
        //horizontal
        assertFalse(sb.isValidOld(1,1));
        //vertical
        assertFalse(sb.isValidOld(1,9));
        //square
        assertFalse(sb.isValidOld(1,47));
    }

    @Test
    public void testOldValidateAndNew(){

    }
}
