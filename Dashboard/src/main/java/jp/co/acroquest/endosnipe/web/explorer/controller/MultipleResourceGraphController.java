/*******************************************************************************
 * ENdoSnipe 5.0 - (https://github.com/endosnipe)
 *
 * The MIT License (MIT)
 *
 * Copyright (c) 2012 Acroquest Technology Co.,Ltd.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package jp.co.acroquest.endosnipe.web.explorer.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.acroquest.endosnipe.common.util.MessageUtil;
import jp.co.acroquest.endosnipe.data.entity.JavelinMeasurementItem;
import jp.co.acroquest.endosnipe.web.explorer.constants.ResponseConstants;
import jp.co.acroquest.endosnipe.web.explorer.dto.MultipleResourceGraphDefinitionDto;
import jp.co.acroquest.endosnipe.web.explorer.dto.ResponseDto;
import jp.co.acroquest.endosnipe.web.explorer.entity.MultipleResourceGraphInfo;
import jp.co.acroquest.endosnipe.web.explorer.manager.ResourceSender;
import jp.co.acroquest.endosnipe.web.explorer.service.MultipleResourceGraphService;
import net.arnx.jsonic.JSON;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wgp.manager.WgpDataManager;

/**
 * Controller of MultipleResourceGraph.
 *
 * @author pin
 *
 */
@Controller
@RequestMapping("/multipleResourceGraph")
public class MultipleResourceGraphController
{
    /** Wgpのデータを管理するクラス。 */
    @Autowired
    protected WgpDataManager wgpDataManager_;

    /** リソースを送信するクラスのオブジェクト。 */
    @Autowired
    protected ResourceSender resourceSender_;

    /** Service Object of MultipleResourceGraph. */
    @Autowired
    protected MultipleResourceGraphService multipleResourceGraphService_;

    /**
    * コンストラクタ。
    */
    public MultipleResourceGraphController()
    {

    }

    /**
     * Get the measurementList of multiple Resource Graph.
     *  @param multipleResourceGraphItems
     *            JSON data of regular expression
     * @return JavelinMeasurementItem List
     */
    @RequestMapping(value = "/getAllMeasurementList", method = RequestMethod.POST)
    @ResponseBody
    public List<JavelinMeasurementItem> getAllMeasurementList(
            @RequestParam(value = "multipleResourceGraphItems") final String multipleResourceGraphItems)
    {

        List<JavelinMeasurementItem> measurementValueDtoList;
        try
        {
            measurementValueDtoList =
                    multipleResourceGraphService_.getMeasurementItemName(multipleResourceGraphItems);
            return measurementValueDtoList;

        }
        catch (SQLException ex)
        {

            ex.printStackTrace();
            return new ArrayList<JavelinMeasurementItem>();
        }

    }

    /**
     * Get the measurementList of multiple Resource Graph.
     * @param multipleResourceGraphItems
     *            JSON data of regular expression
     * @return JavelinMeasurementItem List
     */
    @RequestMapping(value = "/getAllMeasurementListPattern", method = RequestMethod.POST)
    @ResponseBody
    public List<JavelinMeasurementItem> getAllMeasurementListPattern(
            @RequestParam(value = "multipleResourceGraphItems") final String multipleResourceGraphItems)
    {

        List<JavelinMeasurementItem> measurementValueDtoList;
        try
        {
            measurementValueDtoList =
                    multipleResourceGraphService_.getMeasurementItemName(multipleResourceGraphItems);
            return measurementValueDtoList;

        }
        catch (SQLException ex)
        {

            ex.printStackTrace();
            return new ArrayList<JavelinMeasurementItem>();
        }

    }

    /**
     * Get All multipleResourceGraph Data.
     * 
     * @return MultipleResourceGraph List
     */
    @RequestMapping(value = "/getAllDefinition", method = RequestMethod.POST)
    @ResponseBody
    public List<MultipleResourceGraphDefinitionDto> getAllDefinition()
    {
        List<MultipleResourceGraphDefinitionDto> mulResGraphDefinitionDtos =
                new ArrayList<MultipleResourceGraphDefinitionDto>();

        mulResGraphDefinitionDtos = multipleResourceGraphService_.getAllMultipleResourceGraph();

        return mulResGraphDefinitionDtos;
    }

    /**
     * Add the new multipleResourceGraph.
     *
     * @param mulResGraphDefinition
     *            JSON data of multipleResourceGraph
     * @return data of new multipleResourceGraph
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDto add(
            @RequestParam(value = "mulResGraphDefinition") final String mulResGraphDefinition)
    {
        ResponseDto responseDto = new ResponseDto();
        MultipleResourceGraphDefinitionDto multipleResourceGraphDefinitionDto =
                JSON.decode(mulResGraphDefinition, MultipleResourceGraphDefinitionDto.class);

        String multipleResourceGraphName =
                multipleResourceGraphDefinitionDto.getMultipleResourceGraphName();
        boolean hasSameMulResGraphName =
                this.multipleResourceGraphService_.hasSamemultipleResourceGraphName(multipleResourceGraphName);
        if (hasSameMulResGraphName)
        {
            String errorMessage = MessageUtil.getMessage("WEWD0141", multipleResourceGraphName);
            responseDto.setResult(ResponseConstants.RESULT_FAILURE);
            responseDto.setMessage(errorMessage);
            return responseDto;
        }

        MultipleResourceGraphInfo multipleResourceGraphInfo =
                this.multipleResourceGraphService_.convertMultipleResourceGraphInfo(multipleResourceGraphDefinitionDto);

        // DBに追加する
        MultipleResourceGraphDefinitionDto addedDefinitionDto =
                this.multipleResourceGraphService_.insertMultipleResourceGraphInfo(multipleResourceGraphInfo);

        responseDto.setData(addedDefinitionDto);
        responseDto.setResult(ResponseConstants.RESULT_SUCCESS);
        return responseDto;
    }

    /**
     * Get the selected multipleResourceGraph data.
     * 
     * @param multipleResourceGraphName : Name of multipleResourceGraph
     * @return data of updated multipleResourceGraph
     */
    @RequestMapping(value = "/getDefinition", method = RequestMethod.POST)
    @ResponseBody
    public MultipleResourceGraphDefinitionDto get(
            @RequestParam(value = "multipleResourceGraphName") final String multipleResourceGraphName)
    {
        MultipleResourceGraphDefinitionDto multipleResourceGraphDefinition =
                this.multipleResourceGraphService_.getmultipleResourceGraphInfo(multipleResourceGraphName);
        return multipleResourceGraphDefinition;
    }

    /**
     * Edit the selected multipleResourceGraph.
     *
     * @param mulResGraphDefinition
     *            JSON data of multipleResourceGraph
     * @return data of updated multipleResourceGraph
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDto edit(
            @RequestParam(value = "mulResGraphDefinition") final String mulResGraphDefinition)
    {
        ResponseDto responseDto = new ResponseDto();
        MultipleResourceGraphDefinitionDto multipleResourceGraphDefinitionDto =
                JSON.decode(mulResGraphDefinition, MultipleResourceGraphDefinitionDto.class);

        MultipleResourceGraphInfo multipleResourceGraphInfo =
                this.multipleResourceGraphService_.convertMultipleResourceGraphInfo(multipleResourceGraphDefinitionDto);

        // DBに登録されている定義を更新する
        MultipleResourceGraphDefinitionDto updatedDefinitionDto =
                this.multipleResourceGraphService_.updatemultipleResourceGraphInfo(multipleResourceGraphInfo);
        responseDto.setResult(ResponseConstants.RESULT_SUCCESS);
        responseDto.setData(updatedDefinitionDto);

        return responseDto;
    }

    /**
     * Delete the selected multipleResourceGraph.
     *
     * @param multipleResourceGraphName
     *            JSON name of multipleResourceGraph
     * @return data of deleted multipleResourceGraph
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDto delete(
            @RequestParam(value = "multipleResourceGraphName") final String multipleResourceGraphName)
    {
        this.multipleResourceGraphService_.deletemultipleResourceGraphInfo(multipleResourceGraphName);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("multipleResourceGraphName", multipleResourceGraphName);

        ResponseDto responseDto = new ResponseDto();
        responseDto.setResult(ResponseConstants.RESULT_SUCCESS);
        responseDto.setData(map);
        return responseDto;
    }
}
