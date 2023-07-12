package fr.epita.assistants.myide.domain.entity.features.any;

import com.sun.jdi.request.ExceptionRequest;
import fr.epita.assistants.myide.domain.entity.Feature_model;
import fr.epita.assistants.myide.domain.entity.Mandatory;
import fr.epita.assistants.myide.domain.entity.Node;
import fr.epita.assistants.myide.domain.entity.Project;
import fr.epita.assistants.myide.domain.entity.features.ExecutionReport_model;

import java.io.*;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class Dist extends Feature_model {
    public Dist() {super (Mandatory.Features.Any.DIST);}

    @Override
    public ExecutionReport execute(Project project, Object... params) {
        project.getFeature(Mandatory.Features.Any.CLEANUP).get().execute(project);
        File fileToZip = new File(project.getRootNode().getPath().toString());
        try {
            FileOutputStream fos = new FileOutputStream(Paths.get(project.getRootNode().getPath().toString() + ".zip").toString());
            ZipOutputStream zipOut = new ZipOutputStream(fos);
            zipFile(fileToZip, fileToZip.getName(), zipOut);
            zipOut.close();
            fos.close();
        } catch (Exception e) {
            return new ExecutionReport_model(false);
        }
        return new ExecutionReport_model(true);
    }

    private static void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
        if (fileToZip.isDirectory()) {
            if (fileName.endsWith("/")) {
                zipOut.putNextEntry(new ZipEntry(fileName));
                zipOut.closeEntry();
            } else {
                zipOut.putNextEntry(new ZipEntry(fileName + "/"));
                zipOut.closeEntry();
            }
            File[] children = fileToZip.listFiles();
            for (File childFile : children) {
                zipFile(childFile, fileName + "/" + childFile.getName(), zipOut);
            }
            return;
        }
        FileInputStream fis = new FileInputStream(fileToZip);
        ZipEntry zipEntry = new ZipEntry(fileName);
        zipOut.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
        }
        fis.close();
    }
}
