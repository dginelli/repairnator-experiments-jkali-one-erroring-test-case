package kg.apc.jmeter.reporters;

import kg.apc.emulators.TestJMeterUtils;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.TestElement;
import org.junit.*;

import java.awt.*;

public class LoadosophiaUploaderGuiTest {

    public LoadosophiaUploaderGuiTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        TestJMeterUtils.createJmeterEnv();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getStaticLabel method, of class LoadosophiaUploaderGui.
     */
    @Test
    public void testGetStaticLabel() {
        System.out.println("getStaticLabel");
        LoadosophiaUploaderGui instance = new LoadosophiaUploaderGui();
        String result = instance.getStaticLabel();
        Assert.assertTrue(result.length() > 0);
    }

    /**
     * Test of getLabelResource method, of class LoadosophiaUploaderGui.
     */
    @Test
    public void testGetLabelResource() {
        System.out.println("getLabelResource");
        LoadosophiaUploaderGui instance = new LoadosophiaUploaderGui();
        String result = instance.getLabelResource();
        Assert.assertTrue(result.length() > 0);
    }

    /**
     * Test of createTestElement method, of class LoadosophiaUploaderGui.
     */
    @Test
    public void testCreateTestElement() {
        System.out.println("createTestElement");
        LoadosophiaUploaderGui instance = new LoadosophiaUploaderGui();
        TestElement result = instance.createTestElement();
        Assert.assertTrue(result instanceof LoadosophiaUploader);
    }

    /**
     * Test of modifyTestElement method, of class LoadosophiaUploaderGui.
     */
    @Test
    public void testModifyTestElement() {
        System.out.println("modifyTestElement");
        TestElement te = new LoadosophiaUploader();
        LoadosophiaUploaderGui instance = new LoadosophiaUploaderGui();
        instance.modifyTestElement(te);
    }

    /**
     * Test of configure method, of class LoadosophiaUploaderGui.
     */
    @Test
    public void testConfigure() {
        System.out.println("configure");
        TestElement element = new LoadosophiaUploader();
        LoadosophiaUploaderGui instance = new LoadosophiaUploaderGui();
        instance.configure(element);
    }

    /**
     * Test of clearGui method, of class LoadosophiaUploaderGui.
     */
    @Test
    public void testClearGui() {
        System.out.println("clearGui");
        LoadosophiaUploaderGui instance = new LoadosophiaUploaderGui();
        instance.clearGui();
    }

    /**
     * Test of add method, of class LoadosophiaUploaderGui.
     */
    @Test
    public void testAdd() {
        System.out.println("add");
        LoadosophiaUploaderGui instance = new LoadosophiaUploaderGui();
        instance.add((SampleResult) null);
    }

    /**
     * Test of clearData method, of class LoadosophiaUploaderGui.
     */
    @Test
    public void testClearData() {
        System.out.println("clearData");
        LoadosophiaUploaderGui instance = new LoadosophiaUploaderGui();
        instance.clearData();
    }

    /**
     * Test of inform method, of class LoadosophiaUploaderGui.
     */
    @Test
    public void testInform() {
        System.out.println("inform");
        String string = "";
        LoadosophiaUploaderGui instance = new LoadosophiaUploaderGui();
        instance.inform(string);
    }

    /**
     * Test of isStats method, of class LoadosophiaUploaderGui.
     */
    @Test
    public void testIsStats() {
        System.out.println("isStats");
        LoadosophiaUploaderGui instance = new LoadosophiaUploaderGui();
        boolean result = instance.isStats();
        Assert.assertEquals(false, result);
    }

    /**
     * Test of getFilePanel method, of class LoadosophiaUploaderGui.
     */
    @Test
    public void testGetFilePanel() {
        System.out.println("getFilePanel");
        LoadosophiaUploaderGui instance = new LoadosophiaUploaderGui();
        Component result = instance.getFilePanel();
        Assert.assertNotNull(result);
    }
}
