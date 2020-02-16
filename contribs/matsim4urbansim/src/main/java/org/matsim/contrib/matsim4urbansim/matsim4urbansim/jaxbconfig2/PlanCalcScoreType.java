//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-558 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.04.19 at 12:45:10 PM CEST 
//


package org.matsim.contrib.matsim4urbansim.matsim4urbansim.jaxbconfig2;

import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for planCalcScoreType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="planCalcScoreType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="activityType_0" type="{http://www.w3.org/2001/XMLSchema}token"/>
 *         &lt;element name="activityType_1" type="{http://www.w3.org/2001/XMLSchema}token"/>
 *         &lt;element name="homeActivityTypicalDuration" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger"/>
 *         &lt;element name="workActivityTypicalDuration" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger"/>
 *         &lt;element name="workActivityOpeningTime" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger"/>
 *         &lt;element name="workActivityLatestStartTime" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "planCalcScoreType", propOrder = {
    "activityType0",
    "activityType1",
    "homeActivityTypicalDuration",
    "workActivityTypicalDuration",
    "workActivityOpeningTime",
    "workActivityLatestStartTime"
})
public class PlanCalcScoreType {

    @XmlElement(name = "activityType_0", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String activityType0;
    @XmlElement(name = "activityType_1", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String activityType1;
    @XmlElement(required = true)
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger homeActivityTypicalDuration;
    @XmlElement(required = true)
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger workActivityTypicalDuration;
    @XmlElement(required = true)
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger workActivityOpeningTime;
    @XmlElement(required = true)
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger workActivityLatestStartTime;

    /**
     * Gets the value of the activityType0 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getActivityType0() {
        return activityType0;
    }

    /**
     * Sets the value of the activityType0 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setActivityType0(String value) {
        this.activityType0 = value;
    }

    /**
     * Gets the value of the activityType1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getActivityType1() {
        return activityType1;
    }

    /**
     * Sets the value of the activityType1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setActivityType1(String value) {
        this.activityType1 = value;
    }

    /**
     * Gets the value of the homeActivityTypicalDuration property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getHomeActivityTypicalDuration() {
        return homeActivityTypicalDuration;
    }

    /**
     * Sets the value of the homeActivityTypicalDuration property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setHomeActivityTypicalDuration(BigInteger value) {
        this.homeActivityTypicalDuration = value;
    }

    /**
     * Gets the value of the workActivityTypicalDuration property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getWorkActivityTypicalDuration() {
        return workActivityTypicalDuration;
    }

    /**
     * Sets the value of the workActivityTypicalDuration property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setWorkActivityTypicalDuration(BigInteger value) {
        this.workActivityTypicalDuration = value;
    }

    /**
     * Gets the value of the workActivityOpeningTime property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getWorkActivityOpeningTime() {
        return workActivityOpeningTime;
    }

    /**
     * Sets the value of the workActivityOpeningTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setWorkActivityOpeningTime(BigInteger value) {
        this.workActivityOpeningTime = value;
    }

    /**
     * Gets the value of the workActivityLatestStartTime property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getWorkActivityLatestStartTime() {
        return workActivityLatestStartTime;
    }

    /**
     * Sets the value of the workActivityLatestStartTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setWorkActivityLatestStartTime(BigInteger value) {
        this.workActivityLatestStartTime = value;
    }

}
