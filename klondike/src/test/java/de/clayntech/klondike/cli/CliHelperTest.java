package de.clayntech.klondike.cli;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.Arrays;

@SuppressWarnings("SpellCheckingInspection")
public class CliHelperTest {

    @Test
    @Timeout(2)
    public void testAnalyzeInput1() {
        String input="echo abc def";
        String[] args=CliHelper.analyzeInput(input);
        Assertions.assertEquals(2,args.length);
        Assertions.assertArrayEquals(new String[]{"abc","def"},args);
    }

    @Test
    @Timeout(2)
    public void testAnalyzeInput2() {
        String input="echo \"abc def\"";
        String[] args=CliHelper.analyzeInput(input);
        Assertions.assertEquals(1,args.length);
        Assertions.assertArrayEquals(new String[]{"abc def"},args);
    }

    @Test
    @Timeout(2)
    public void testAnalyzeInput3() {
        String input="echo \\\"abc def\"";
        String[] args=CliHelper.analyzeInput(input);
        Assertions.assertEquals(2,args.length, Arrays.toString(args));
        Assertions.assertArrayEquals(new String[]{"\"abc","def"},args);
    }

    @Test
    @Timeout(2)
    public void testAnalyzeInput4() {
        String input="echo \"abc def\" ghi";
        String[] args=CliHelper.analyzeInput(input);
        Assertions.assertEquals(2,args.length, Arrays.toString(args));
        Assertions.assertArrayEquals(new String[]{"abc def","ghi"},args);
    }

    @Test
    @Timeout(2)
    public void testAnalyzeInput5() {
        String input="echo \"a\\\"bc def\" ghi";
        String[] args=CliHelper.analyzeInput(input);
        Assertions.assertEquals(2,args.length, Arrays.toString(args));
        Assertions.assertArrayEquals(new String[]{"a\"bc def","ghi"},args);
    }

    @Test
    @Timeout(2)
    public void testAnalyzeInput6() {
        String input="echo";
        String[] args=CliHelper.analyzeInput(input);
        Assertions.assertEquals(0,args.length, Arrays.toString(args));
    }

    @Test
    @Timeout(2)
    public void testAnalyzeInput7() {
        String input="echo \"abcd\\def\\\"ghi\"";
        String[] args=CliHelper.analyzeInput(input);
        Assertions.assertEquals(1,args.length, Arrays.toString(args));
        Assertions.assertArrayEquals(new String[]{"abcd\\def\"ghi"},args);
    }
}
