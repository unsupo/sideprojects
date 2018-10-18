import java.io.File;
import java.math.BigInteger;

public class SudokuBits {
    public static void main(String[] args) throws Exception {
        SudokuBits sb = new SudokuBits(new int[]{
                1, -1, -1, 2, -1, -1, -1, -1, -1,
                4, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1,
                5, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1,
                7, -1, -1, 5, -1, -1, -1, -1, 4,
                -1, -1, -1, -1, -1, -1, -1, -1, -1,
                9, -1, -1, -1, -1, -1, -1, -1, -1,
                2, -1, -1, -1, -1, -1, -1, 6, -1
        });
        System.out.println(print(solve(sb)));
//        char[] s = BitHelper.fill(sb.orAll().toString(2),size).toCharArray();
//        String r = "";
//        for (int i = 0; i < s.length; i++) {
//            r+=s[i];
//            if((i+1)%lsize==0)
//                r+="\n";
//        }
//        System.out.println(r);
//        System.out.println(sb.isValid(1,13));
//        System.out.println(print(sb));
    }

    //        // TODO: 10/17/18 convert to int[]
//        int[] v = new int[size];
//        String[] ss = new String[s.length];
//        for (int j = 0; j < s.length; j++)
//            ss[j]=BitHelper.fill(s[j].toString(2),size);
//        for (int j = 0; j < size; j++) {
//            for (int k = 0; k < ss.length; k++) {
//                char[] cc = ss[k].toCharArray();
//                if(cc[j]=='1')
//                    v[j]=k;
//            }
//        }
//         return v;
    private static BigInteger
            horizontal   = new BigInteger("111111111000000000000000000000000000000000000000000000000000000000000000000000000",2),
            vertical     = new BigInteger("100000000100000000100000000100000000100000000100000000100000000100000000100000000",2),
            square       = new BigInteger("111000000111000000111000000000000000000000000000000000000000000000000000000000000",2);
    private static String position = "000000000000000000000000000000000000000000000000000000000000000000000000000000000";
    private static int lsize = 9, size = lsize*lsize;

    private int[] startingNumbers = new int[size];
    private BigInteger[] numbers = new BigInteger[lsize];

    public SudokuBits(){}

    public SudokuBits(int[] startingNumbers, BigInteger[] numbers) {
        this.startingNumbers = startingNumbers;
        this.numbers = numbers;
    }

    public SudokuBits(int[] startingNumbers){
        if(startingNumbers.length != size) throw new IllegalArgumentException("Invalid length");
        this.startingNumbers = startingNumbers;
        String[] bits = new String[numbers.length];
        for (int i = 0; i < bits.length; i++)
            bits[i]="";
        for (int i = 0; i < startingNumbers.length; i++)
            for (int j = 0; j < bits.length; j++)
                if((j+1)==startingNumbers[i])
                    bits[j]+="1";
                else
                    bits[j]+="0";
        for (int i = 0; i < bits.length; i++)
            numbers[i] = new BigInteger(bits[i],2);
    }

    private boolean checker(BigInteger a, BigInteger b, BigInteger checker, int size){
        BigInteger s = checker;
        for (int i = 0; i < lsize; i++){
            //if a is in this check (row/column/square)
            if(a.and(s).equals(a)) { //no need for a second loop because this is the test row
                //and if b has a number in this row then invalid item
                if (!b.and(s).equals(BigInteger.ZERO))
                    return false;
                return true;
            }
            s = s.shiftRight(size);
        }
        return true;
    }
    private BigInteger getNumAtPos(int pos){
        char[] c = position.toCharArray();
        c[pos]='1';
        return new BigInteger(String.valueOf(c),2);
    }
    public boolean isValid(int num, int pos){
        BigInteger numpos = numbers[num-1];
        BigInteger number = getNumAtPos(pos);
        for (int i = 0; i < numbers.length; i++)
            if(!numbers[i].equals(BigInteger.ZERO) && number.and(numbers[i]).equals(numbers[i]))
                return false;
        //check horizontal
        if(!checker(number,numpos,horizontal,lsize))
            return false;
        //check vertical
        if(!checker(number,numpos,vertical,1))
            return false;
        //check square
        if(!checker(number,numpos,square,3))
            return false;
        return true;
    }

    private BigInteger orAll() {
        BigInteger b = numbers[0];
        for (int i = 1; i < numbers.length; i++)
            b = b.or(numbers[i]);
        return b;
    }

    @Override
    public SudokuBits clone(){
        return new SudokuBits(startingNumbers,numbers.clone());
    }

    //driver
    public static SudokuBits solve(SudokuBits sb){
        //get all places without a number
        char[] c = sb.orAll().toString(2).toCharArray();
        int i;
        for (i = 0; i < c.length; i++)
            if(c[i]=='0') break;
        return _solve(sb, c, i);
    }private static SudokuBits _solve(SudokuBits sb, char[] c, int i){
        if(i>=size)
            return sb;
        for (int j = 1; j <= 9; j++) {
            if (sb.isValid(j, i)) {
                SudokuBits clone = sb.clone();
                clone.putNumber(j, i);
                char[] cc = clone.orAll().toString(2).toCharArray();
                int k;
                for (k = i; k < c.length; k++)
                    if(cc[k]=='0') break;
                if(k>=size)
                    return clone;
                SudokuBits s = _solve(clone, cc, k);
                if(s.orAll().bitCount()==size)
                    return s;
            }
        }
        return sb;
    }

    public void putNumber(int num, int pos) {
        numbers[num-1]=numbers[num-1].or(getNumAtPos(pos));
    }

    public static String print(SudokuBits sb){
        String[] b = new String[sb.numbers.length];
        for (int i = 0; i < sb.numbers.length; i++)
            b[i]=BitHelper.fill(sb.numbers[i].toString(2),size);
        String f = "";
        for (int j = 0; j < b[0].toCharArray().length; j++) {
            boolean isSomething = false;
            for (int i = 0; i < b.length; i++)
                if (b[i].toCharArray()[j] == '1'){
                    f += "" + (i + 1);
                    isSomething = true;
                }
            if(!isSomething) f+="-";
            if((j+1)%3==0) f+="|";
            if((j+1)%lsize==0) {
                f += "\n";
                if((j+1)%(3*9)==0) {
                    for (int i = 0; i < lsize + 3; i++)
                        f += "-";
                    f += "\n";
                }
            }
        }
        return f;
    }

    private static void getAllPositions(){
        int r = (int) Math.sqrt(lsize);
        String h = "", v = "", s = "";
        for (int i = 0; i < size; i++) {
            //horizontal
            if(i<lsize) h+="1";
            else h+="0";
            //vertical
            if(i==0 || i%lsize==0) v+="1";
            else v+="0";
            //square
            if(i<r || (i>=lsize && i<lsize+r) || (i>=lsize*2 && i<lsize*2+r)) s+="1";
            else s+="0";
        }
        String f = "new BigInteger(\"%s\",2);";
        System.out.println(String.format(f,h));
        System.out.println(String.format(f,v));
        System.out.println(String.format(f,s));
    }
}
