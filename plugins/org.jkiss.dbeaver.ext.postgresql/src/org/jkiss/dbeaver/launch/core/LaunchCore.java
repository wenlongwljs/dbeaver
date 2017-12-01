package org.jkiss.dbeaver.launch.core;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Adapters;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.jkiss.dbeaver.Log;
import org.jkiss.dbeaver.model.struct.DBSObject;

public class LaunchCore {

    public static final String BUNDLE_SYMBOLIC_NAME = "org.jkiss.dbeaver.launch.core"; //$NON-NLS-1$

    public static boolean canLaunch(ILaunchConfiguration configuration, String mode) {
        if (configuration == null || !configuration.exists()) {
            return false;
        }
        try {
            return configuration.supportsMode(mode);
        } catch (CoreException e) {
            // ignore, we can not launch anyway
            return false;
        }       
    }

    public static List<DBSObject> extractLaunchable(Object[] scope)
    {
        List<DBSObject> extracted = new ArrayList<>();
        if (scope == null) {
            return extracted;
        }
        for (int i = 0; i < scope.length; i++) {
            Object object = scope[i];
            DBSObject adapted = Adapters.adapt(object, DBSObject.class, true);
            if (adapted != null) {
                extracted.add(adapted);
            }
        }
        return extracted;
    }

    public static void log(Log delegate, IStatus status) {
        if (delegate == null) {
            //no way to log
            return;
        }
        if (status == null) {
            //nothing to log
            return;
        }
        int severity = status.getSeverity();
        String message = status.getMessage();
        Throwable exception = status.getException();
        switch (severity) {
        case IStatus.CANCEL:
            delegate.debug(message, exception);
            break;
        case IStatus.ERROR:
            delegate.error(message, exception);
            break;
        case IStatus.WARNING:
            delegate.warn(message, exception);
            break;
        case IStatus.INFO:
            delegate.info(message, exception);
            break;
        case IStatus.OK:
            delegate.trace(message, exception);
            break;
        default:
            break;
        }
    }

}
