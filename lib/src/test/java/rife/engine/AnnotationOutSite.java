/*
 * Copyright 2001-2022 Geert Bevin (gbevin[remove] at uwyn dot com)
 * Licensed under the Apache License, Version 2.0 (the "License")
 */
package rife.engine;

import rife.engine.annotations.*;
import rife.tools.FileUtils;
import rife.tools.exceptions.FileUtilsErrorException;

import java.io.File;

import static rife.engine.annotations.FlowDirection.OUT;

public class AnnotationOutSite extends Site {
    public static class AnnotatedElement implements Element {
        @Body(flow = OUT) String stringBody = "defaultBody";
        @Body(flow = OUT) int intBody = -1;

        @Cookie(flow = OUT) String stringCookie = "defaultCookie";
        @Cookie(flow = OUT) int intCookie = -2;
        @Cookie(name = "cookie2", flow = OUT) String stringCookie2 = "defaultCookie2";
        @Cookie(name = "cookie3", flow = OUT) int intCookie2 = -3;

        @RequestAttribute(flow = OUT) String stringRequestAttribute = "defaultRequestAttribute";
        @RequestAttribute(flow = OUT) int intRequestAttribute = -4;
        @RequestAttribute(name = "requestAttr2", flow = OUT) String stringRequestAttribute2 = "defaultRequestAttribute2";
        @RequestAttribute(name = "requestAttr3", flow = OUT) int intRequestAttribute2 = -5;

        @SessionAttribute(flow = OUT) String stringSessionAttribute = "defaultSessionAttribute";
        @SessionAttribute(flow = OUT) int intSessionAttribute = -6;
        @SessionAttribute(name = "sessionAttr2", flow = OUT) String stringSessionAttribute2 = "defaultSessionAttribute2";
        @SessionAttribute(name = "sessionAttr3", flow = OUT) int intSessionAttribute2 = -7;

        @Header(flow = OUT) String stringHeader = "defaultHeader";
        @Header(flow = OUT) int intHeader = -8;
        @Header(name = "header2", flow = OUT) String stringHeader2 = "defaultHeader2";
        @Header(name = "header3", flow = OUT) int intHeader2 = -9;

        public void process(Context c) {
            if (c.parameterBoolean("generate")) {
                stringBody = "value1";
                intBody = 2;
                stringCookie = "value3";
                intCookie = 4;
                stringCookie2 = "value5";
                intCookie2 = 6;
                stringRequestAttribute = "value7";
                intRequestAttribute = 8;
                stringRequestAttribute2 = "value9";
                intRequestAttribute2 = 10;
                stringSessionAttribute = "value11";
                intSessionAttribute = 12;
                stringSessionAttribute2 = "value13";
                intSessionAttribute2 = 14;
                stringHeader = "value15";
                intHeader = 16;
                stringHeader2 = "value17";
                intHeader2 = 18;
            }
        }
    }

    public void setup() {
        get("/get", AnnotatedElement.class);
        after(c -> {
            c.print(c.attribute("stringRequestAttribute"));
            c.print(c.attribute("intRequestAttribute"));
            c.print(c.attribute("requestAttr2"));
            c.print(c.attribute("requestAttr3"));
            c.print(c.session(false).getAttribute("stringSessionAttribute"));
            c.print(c.session(false).getAttribute("intSessionAttribute"));
            c.print(c.session(false).getAttribute("sessionAttr2"));
            c.print(c.session(false).getAttribute("sessionAttr3"));
        });
    }
}