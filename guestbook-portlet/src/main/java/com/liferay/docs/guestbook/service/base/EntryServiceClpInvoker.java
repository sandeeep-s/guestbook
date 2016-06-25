package com.liferay.docs.guestbook.service.base;

import com.liferay.docs.guestbook.service.EntryServiceUtil;

import java.util.Arrays;

/**
 * @author Brian Wing Shun Chan
 * @generated
 */
public class EntryServiceClpInvoker {
    private String _methodName22;
    private String[] _methodParameterTypes22;
    private String _methodName23;
    private String[] _methodParameterTypes23;

    public EntryServiceClpInvoker() {
        _methodName22 = "getBeanIdentifier";

        _methodParameterTypes22 = new String[] {  };

        _methodName23 = "setBeanIdentifier";

        _methodParameterTypes23 = new String[] { "java.lang.String" };
    }

    public Object invokeMethod(String name, String[] parameterTypes,
        Object[] arguments) throws Throwable {
        if (_methodName22.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes22, parameterTypes)) {
            return EntryServiceUtil.getBeanIdentifier();
        }

        if (_methodName23.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes23, parameterTypes)) {
            EntryServiceUtil.setBeanIdentifier((java.lang.String) arguments[0]);

            return null;
        }

        throw new UnsupportedOperationException();
    }
}
