package com.molla.admin.AbstractExporter;

import com.molla.common.entity.User;

import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AbstractExporter {
    public  void setResponseHader( HttpServletResponse response, String contentType, String extension, String prefix){
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String timestamp = dateFormatter.format(new Date());
        String fileName = prefix + timestamp +  extension;

        response.setContentType(contentType);

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename" + fileName;

        response.setHeader(headerKey, headerValue);
    }
}
