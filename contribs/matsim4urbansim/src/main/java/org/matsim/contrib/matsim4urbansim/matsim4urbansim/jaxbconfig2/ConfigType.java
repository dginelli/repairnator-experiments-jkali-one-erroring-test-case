//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-558 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.04.19 at 12:45:10 PM CEST 
//


package org.matsim.contrib.matsim4urbansim.matsim4urbansim.jaxbconfig2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for configType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="configType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="matsim_config" type="{}fileType"/>
 *         &lt;element name="network" type="{}fileType"/>
 *         &lt;element name="inputPlansFile" type="{}inputPlansFileType"/>
 *         &lt;element name="hotStartPlansFile" type="{}inputPlansFileType"/>
 *         &lt;element name="controler" type="{}controlerType"/>
 *         &lt;element name="planCalcScore" type="{}planCalcScoreType"/>
 *         &lt;element name="strategy" type="{}strategyType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "configType", propOrder = {
    "matsimConfig",
    "network",
    "inputPlansFile",
    "hotStartPlansFile",
    "controler",
    "planCalcScore",
    "strategy"
})
public class ConfigType {

    @XmlElement(name = "matsim_config", required = true)
    protected FileType matsimConfig;
    @XmlElement(required = true)
    protected FileType network;
    @XmlElement(required = true)
    protected InputPlansFileType inputPlansFile;
    @XmlElement(required = true)
    protected InputPlansFileType hotStartPlansFile;
    @XmlElement(required = true)
    protected ControlerType controler;
    @XmlElement(required = true)
    protected PlanCalcScoreType planCalcScore;
    @XmlElement(required = true)
    protected StrategyType strategy;

    /**
     * Gets the value of the matsimConfig property.
     * 
     * @return
     *     possible object is
     *     {@link FileType }
     *     
     */
    public FileType getMatsimConfig() {
        return matsimConfig;
    }

    /**
     * Sets the value of the matsimConfig property.
     * 
     * @param value
     *     allowed object is
     *     {@link FileType }
     *     
     */
    public void setMatsimConfig(FileType value) {
        this.matsimConfig = value;
    }

    /**
     * Gets the value of the network property.
     * 
     * @return
     *     possible object is
     *     {@link FileType }
     *     
     */
    public FileType getNetwork() {
        return network;
    }

    /**
     * Sets the value of the network property.
     * 
     * @param value
     *     allowed object is
     *     {@link FileType }
     *     
     */
    public void setNetwork(FileType value) {
        this.network = value;
    }

    /**
     * Gets the value of the inputPlansFile property.
     * 
     * @return
     *     possible object is
     *     {@link InputPlansFileType }
     *     
     */
    public InputPlansFileType getInputPlansFile() {
        return inputPlansFile;
    }

    /**
     * Sets the value of the inputPlansFile property.
     * 
     * @param value
     *     allowed object is
     *     {@link InputPlansFileType }
     *     
     */
    public void setInputPlansFile(InputPlansFileType value) {
        this.inputPlansFile = value;
    }

    /**
     * Gets the value of the hotStartPlansFile property.
     * 
     * @return
     *     possible object is
     *     {@link InputPlansFileType }
     *     
     */
    public InputPlansFileType getHotStartPlansFile() {
        return hotStartPlansFile;
    }

    /**
     * Sets the value of the hotStartPlansFile property.
     * 
     * @param value
     *     allowed object is
     *     {@link InputPlansFileType }
     *     
     */
    public void setHotStartPlansFile(InputPlansFileType value) {
        this.hotStartPlansFile = value;
    }

    /**
     * Gets the value of the controler property.
     * 
     * @return
     *     possible object is
     *     {@link ControlerType }
     *     
     */
    public ControlerType getControler() {
        return controler;
    }

    /**
     * Sets the value of the controler property.
     * 
     * @param value
     *     allowed object is
     *     {@link ControlerType }
     *     
     */
    public void setControler(ControlerType value) {
        this.controler = value;
    }

    /**
     * Gets the value of the planCalcScore property.
     * 
     * @return
     *     possible object is
     *     {@link PlanCalcScoreType }
     *     
     */
    public PlanCalcScoreType getPlanCalcScore() {
        return planCalcScore;
    }

    /**
     * Sets the value of the planCalcScore property.
     * 
     * @param value
     *     allowed object is
     *     {@link PlanCalcScoreType }
     *     
     */
    public void setPlanCalcScore(PlanCalcScoreType value) {
        this.planCalcScore = value;
    }

    /**
     * Gets the value of the strategy property.
     * 
     * @return
     *     possible object is
     *     {@link StrategyType }
     *     
     */
    public StrategyType getStrategy() {
        return strategy;
    }

    /**
     * Sets the value of the strategy property.
     * 
     * @param value
     *     allowed object is
     *     {@link StrategyType }
     *     
     */
    public void setStrategy(StrategyType value) {
        this.strategy = value;
    }

}
