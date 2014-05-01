package org.wgp.manager;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.wgp.dto.ReceiveDto;

public class WgpDataManagerTest
{

    @Test
    public void testUpdateData()
    {
        WgpDataManager wgpManager = new WgpDataManager();
        SampleObject object = new SampleObject();
        object.setId("aaa");
        object.setNameId(new Integer(2));
        wgpManager.setData("id", "2", object);
        object.setValue("value");
        SampleObject updateObject = new SampleObject();
        updateObject.setId("bbb");
        wgpManager.setData("id", "2", updateObject);
        SampleObject result = (SampleObject)wgpManager.getData("id", "2");
        String id = result.getId();
        Integer nameId = result.getNameId();
        String value = result.getValue();
        assertEquals(id, "bbb");
        assertEquals(nameId, new Integer(2));
        assertEquals(value, "value");
    }

    @Test
    public void testSetData_addData()
    {
        ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
        WgpDataManager wgpDataManager = (WgpDataManager)context.getBean("wgpDataManager");
        ReceiveDto receiveDto = new ReceiveDto();
        receiveDto.setConnectType("ajax");
        wgpDataManager.setData("01", "01", receiveDto);
        ReceiveDto receiveDto2 = new ReceiveDto();
        receiveDto2.setConnectType("comet");
        wgpDataManager.setData("01", "02", receiveDto2);
    }

    @Test
    public void testSetData_updateData()
    {
        ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
        WgpDataManager wgpDataManager = (WgpDataManager)context.getBean("wgpDataManager");
        ReceiveDto receiveDto = new ReceiveDto();
        receiveDto.setConnectType("ajax");
        wgpDataManager.setData("01", "01", receiveDto);
        ReceiveDto receiveDto2 = new ReceiveDto();
        receiveDto2.setConnectType("comet");
        wgpDataManager.updateData("01", "01", receiveDto2);
    }

    private class SampleObject
    {
        private String id;

        private String value;

        private Integer nameId;

        public String getId()
        {
            return id;
        }

        public void setId(final String id)
        {
            this.id = id;
        }

        public String getValue()
        {
            return value;
        }

        public void setValue(final String value)
        {
            this.value = value;
        }

        public Integer getNameId()
        {
            return nameId;
        }

        public void setNameId(final Integer nameId)
        {
            this.nameId = nameId;
        }
    }

}
