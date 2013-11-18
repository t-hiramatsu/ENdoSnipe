package jp.co.acroquest.endosnipe.javelin.converter.resource;

import java.io.IOException;
import java.util.List;

import jp.co.acroquest.endosnipe.javelin.converter.AbstractConverter;
import jp.co.acroquest.endosnipe.javelin.converter.resource.monitor.TimedResourceMonitor;
import jp.co.smg.endosnipe.javassist.CannotCompileException;
import jp.co.smg.endosnipe.javassist.CtBehavior;
import jp.co.smg.endosnipe.javassist.CtClass;
import jp.co.smg.endosnipe.javassist.NotFoundException;

/**
 * リソース情報を受け取るインターフェースを埋め込むコンバータ。
 * 
 * @author eriguchi
 *
 */
public class ResourceGetterConverter extends AbstractConverter
{

    @Override
    public void convertImpl()
        throws CannotCompileException,
            NotFoundException,
            IOException
    {
        CtClass ctClass = getCtClass();

        List<CtBehavior> behaviorList = getMatcheDeclaredBehavior();
        for (CtBehavior ctBehavior : behaviorList)
        {
            if (ctBehavior.getMethodInfo2().getName().equals("addValue"))
            {
                convertMethod(ctBehavior);
            }
            if (ctBehavior.getMethodInfo2().getName().equals("send"))
            {
                convertSendMethod(ctBehavior);
            }
        }

        setNewClassfileBuffer(ctClass.toBytecode());
    }

    private void convertSendMethod(CtBehavior ctBehavior)
        throws CannotCompileException
    {
        String canonicalName = TimedResourceMonitor.class.getCanonicalName();
        String sendValueString = canonicalName + ".sendAll($1);";
        ctBehavior.insertBefore(sendValueString);

        // 処理結果をログに出力する。
        logModifiedMethod("ResourceGetterConverter", ctBehavior);
    }

    private void convertMethod(final CtBehavior ctMethod)
        throws CannotCompileException
    {
        String canonicalName = TimedResourceMonitor.class.getCanonicalName();
        String addValueString = canonicalName + ".addValue($1, $2);";
        ctMethod.insertBefore(addValueString);

        // 処理結果をログに出力する。
        logModifiedMethod("ResourceGetterConverter", ctMethod);
    }

    /** (non-Javadoc)
     * @see jp.co.acroquest.endosnipe.javelin.converter.Converter#init()
     */
    public void init()
    {
        // 何もしない。
    }

}
