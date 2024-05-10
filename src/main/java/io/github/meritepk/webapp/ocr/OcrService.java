package io.github.meritepk.webapp.ocr;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URI;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import net.sourceforge.tess4j.Tesseract;

@Service
public class OcrService {

    private final String tessdataLocal;
    private final String tessdataRemote;

    public OcrService(@Value("${tesseract-ocr.tessdata.local}") String tessdataLocal,
            @Value("${tesseract-ocr.tessdata.remote}") String tessdataRemote) {
        this.tessdataLocal = tessdataLocal;
        this.tessdataRemote = tessdataRemote;
    }

    public String extract(InputStream input, String language, String mode) throws Exception {
        // String language = "urd";
        // String mode = "_best";
        if ("fast".equals(mode)) {
            mode = "_fast";
        } else if ("best".equals(mode)) {
            mode = "_best";
        } else {
            mode = "";
        }
        if (!StringUtils.hasText(language)) {
            language = "eng";
        }
        String tessdata = tessdataLocal.replace("{mode}", mode).replace("{language}", language);
        File tessdataFile = new File(tessdata);
        if (!tessdataFile.exists()) {
            if (!tessdataFile.getParentFile().exists()) {
                tessdataFile.getParentFile().mkdirs();
            }
            String trainedDataUrl = tessdataRemote.replace("{mode}", mode).replace("{language}", language);
            try (InputStream is = URI.create(trainedDataUrl).toURL().openStream();
                    FileOutputStream fos = new FileOutputStream(tessdataFile)) {
                StreamUtils.copy(is, fos);
            }
        }
        Tesseract tesseract = new Tesseract();
        tesseract.setLanguage(language);
        tesseract.setDatapath(tessdataFile.getParentFile().getAbsolutePath());
        // tesseract.setPageSegMode(ITessAPI.TessPageSegMode.PSM_AUTO_OSD);
        // tesseract.setOcrEngineMode(ITessAPI.TessOcrEngineMode.OEM_LSTM_ONLY);
        // tesseract.setTessVariable("user_defined_dpi", "96");
        return tesseract.doOCR(ImageIO.read(input));
    }
}
