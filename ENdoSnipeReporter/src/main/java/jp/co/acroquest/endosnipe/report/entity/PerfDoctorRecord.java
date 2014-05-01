package jp.co.acroquest.endosnipe.report.entity;

import jp.co.acroquest.endosnipe.perfdoctor.WarningUnit;

/**
 * PerformanceDoctor���|�[�g�ɏo�͂���f�[�^�̒��́A
 * 1���R�[�h���̏���ێ�����G���e�B�e�B�ł��B
 * 
 * @author T. Iida
 */
public class PerfDoctorRecord
{
    /** ID */
    private String id_ = "default id";
    
    /** �T�v */
    private String description_ = "default description";
    
    /** �d�v�x */
    private String level_ = "default level";
    
    /** �N���X */
    private String className_ = "default className";
    
    /** ���\�b�h */
    private String methodName_ = "default methodName";
    
    /** �t�@�C���� */
    private String logFileName_ = "default logFileName";
    
    /**
     * �R���X�g���N�^�ł��B
     */
    public PerfDoctorRecord()
    {
        // �������Ȃ�
    }
    
    /**
     * �R���X�g���N�^�ł��B
     * �w�肳�ꂽWarningUnit�ŁA�t�B�[���h�����������܂��B
     * 
     * @param warningUnit �w�肳�ꂽWarningUnit
     */
    public PerfDoctorRecord(WarningUnit warningUnit)
    {
        this.setId(warningUnit.getId());
        this.setDescription(warningUnit.getDescription());
        this.setLevel(warningUnit.getLevel());
        this.setClassName(warningUnit.getClassName());
        this.setMethodName(warningUnit.getMethodName());
        this.setLogFileName(warningUnit.getLogFileName());
    }

    /**
     * @return the id
     */
    public String getId()
    {
        return this.id_;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id)
    {
        this.id_ = id;
    }

    /**
     * @return the description
     */
    public String getDescription()
    {
        return this.description_;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description)
    {
        this.description_ = description;
    }

    /**
     * @return the level
     */
    public String getLevel()
    {
        return this.level_;
    }

    /**
     * @param level the level to set
     */
    public void setLevel(String level)
    {
        this.level_ = level;
    }

    /**
     * @return the className
     */
    public String getClassName()
    {
        return this.className_;
    }

    /**
     * @param className the className to set
     */
    public void setClassName(String className)
    {
        this.className_ = className;
    }

    /**
     * @return the methodName
     */
    public String getMethodName()
    {
        return this.methodName_;
    }

    /**
     * @param methodName the methodName to set
     */
    public void setMethodName(String methodName)
    {
        this.methodName_ = methodName;
    }

    /**
     * @return the logFileName
     */
    public String getLogFileName()
    {
        return this.logFileName_;
    }

    /**
     * @param logFileName the logFileName to set
     */
    public void setLogFileName(String logFileName)
    {
        this.logFileName_ = logFileName;
    }
}
