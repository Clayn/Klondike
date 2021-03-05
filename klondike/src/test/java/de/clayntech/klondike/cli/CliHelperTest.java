package de.clayntech.klondike.cli;

import de.clayntech.klondike.impl.exec.LogStep;
import de.clayntech.klondike.sdk.exec.Step;
import de.clayntech.klondike.sdk.param.StepParameter;
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

    @Test
    public void testCreateStep() throws Exception {
        String[] args=new String[]{LogStep.class.getName(),"-SP"+LogStep.MESSAGE_PARAMETER+"=Test"};
        Step step=CliHelper.parseStep(args);
        Assertions.assertNotNull(step);
        Assertions.assertEquals(LogStep.class,step.getClass());
        Assertions.assertEquals(1,step.getParameter().size());
        Assertions.assertEquals("Test",step.getParameter().get(0).getValue());
    }

    @Test
    public void testCreateStep2() throws Exception {
        String[] args=new String[]{LogStep.class.getName(),"-SP"+LogStep.MESSAGE_PARAMETER+"=Test2","-SPTest=Test"};
        Step step=CliHelper.parseStep(args);
        Assertions.assertNotNull(step);
        Assertions.assertEquals(LogStep.class,step.getClass());
        Assertions.assertEquals(2,step.getParameter().size());
        for(StepParameter parameter: step.getParameter()) {
            Assertions.assertEquals(String.class,parameter.getTypeClass());
            if(parameter.getName().equals("Test")) {
                Assertions.assertEquals("Test",parameter.getValue());
            }else {
                Assertions.assertEquals("Test2",parameter.getValue());
            }
        }
    }

    @Test
    public void testCreateStep3() throws Exception {
        String[] args=new String[]{LogStep.class.getName(),"-SP"+LogStep.MESSAGE_PARAMETER+"=Test2","-SPTest=Test","-SPTest2=Test3","-CTest2=de.clayntech.klondike.cli.CliHelperTest"};
        Step step=CliHelper.parseStep(args);
        Assertions.assertNotNull(step);
        Assertions.assertEquals(LogStep.class,step.getClass());
        Assertions.assertEquals(2,step.getParameter().size());
        for(StepParameter parameter: step.getParameter()) {
            Assertions.assertEquals(String.class,parameter.getTypeClass());
            if(parameter.getName().equals("Test")) {
                Assertions.assertEquals("Test",parameter.getValue());
            }else {
                Assertions.assertEquals("Test2",parameter.getValue());
            }
        }
    }
}
