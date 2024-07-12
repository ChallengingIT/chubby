package it.challenging.torchy.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import it.challenging.torchy.entity.Pass;
import it.challenging.torchy.repository.PassRepository;
import it.challenging.torchy.repository.UserRepository;
import it.challenging.torchy.response.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/qr")
public class QrCodeController {

    @Autowired
    private PassRepository passRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/code/{username}")
    public ResponseEntity<?> generateQRCodeImage(
            @PathVariable("username") String username
    ) throws Exception {

        if (userRepository.findByUsername(username).isEmpty()) {

            return ResponseEntity.badRequest().body(new MessageResponse("Username non esistente"));

        } else {

            QRCodeWriter barcodeWriter = new QRCodeWriter();

            String url = "http://89.46.196.60:8443/api/auth/mapp/code/verify/" + username;

            BitMatrix bitMatrix = barcodeWriter.encode(url, BarcodeFormat.QR_CODE, 200, 200);

            Pass pass = new Pass();
            pass.setUsername(username);
            pass.setConsumed((byte) 0);

            passRepository.save(pass);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(MatrixToImageWriter.toBufferedImage(bitMatrix), "png", byteArrayOutputStream);

            byte[] imageInByte = byteArrayOutputStream.toByteArray();

            return ResponseEntity.status(HttpStatus.OK)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "filename=\"qrcode.png\"")
                    .contentType(MediaType.IMAGE_PNG)
                    .body(imageInByte);
        }
    }

}
