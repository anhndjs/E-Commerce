package com.molla.admin.exportcsv;

import com.molla.admin.AbstractExporter.AbstractExporter;
import com.molla.common.entity.User;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class UserCsvExporter extends AbstractExporter {
    public void export(List<User> listUsers, HttpServletResponse response) throws IOException {
       super.setResponseHader(response, "text/csv", ".csv", null);

        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
        String [] csvHeader = {"User ID", "E-mail", "First Name", " Last Name ", "Roles", "Enable"};
        String[] fieldMapping = {"id", "email", "firstName", "lastName", "RoleSet", "enabled"};

        csvWriter.writeHeader(csvHeader);
        for(User user : listUsers) {
            csvWriter.write(user, fieldMapping);
        }
        csvWriter.close();

    }
}
