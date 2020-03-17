package com.alipay.sofa.common.utils;

import org.junit.Test;

/**
 * AssertUtil Tester.
 *
 * @author <guanchao.ygc>
 * @version 1.0
 * @since <pre>六月 4, 2018</pre>
 */
public class AssertUtilTest {

    /**
     * Method: isTrue(boolean expression, String message)
     */
    @Test
    public void testIsTrueForExpressionMessage() throws Exception {
        boolean isSuccess = false;
        AssertUtil.isTrue(!isSuccess, "isTrue");
        AssertUtil.isTrue(isSuccess, "isTrue");
    }

    /**
     * Method: isNull(Object object, String message)
     */
    @Test
    public void testIsNullForObjectMessage() throws Exception {
        Object object = null;
        AssertUtil.isNull(object, "null");
        AssertUtil.isNull(object);
    }
}
