package com.example.bookshopwebapplication.utils.mail;

import com.example.bookshopwebapplication.dto.UserDto;
import org.jetbrains.annotations.NotNull;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.*;

public class EmailUtils {
    private static Set<VerificationToken> verificationTokenSet = instance();
    private static Properties properties = new Properties();
    private static ResourceBundle bundle = ResourceBundle.getBundle("emailConfig");

    private static Set<VerificationToken> instance() {
        return new HashSet<>();
    }

    public static void sendEmail(UserDto user, String code) {
        // sets SMTP server properties
        properties.put("mail.smtp.host", bundle.getString("HOST"));
        properties.put("mail.smtp.port", bundle.getString("TSL_PORT"));
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        // creates a new session with an authenticator
        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(bundle.getString("MY_EMAIL"),
                        bundle.getString("MY_PASSWORD"));
            }
        };

        Session session = Session.getInstance(properties, auth);

        // creates a new e-mail message
        try {
            verificationTokenSet.add(new VerificationToken(
                    user.getEmail(),
                    code,
                    System.currentTimeMillis() + 5 * 60 * 1000 // expirationTime = 5 minutes
            ));

            String emailContent = getEmailContent(user.getEmail(), user.getFullName(), code);
            String subject = "Thư xác nhận Email từ BookShopWeb Application";
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(bundle.getString("MY_EMAIL")));
            InternetAddress[] toAddresses = {new InternetAddress(user.getEmail())};
            msg.setRecipients(Message.RecipientType.TO, toAddresses);
            msg.setSubject(subject);
            msg.setSentDate(new Date());
            msg.setContent(emailContent, "text/html; charset=UTF-8");

            // sends the e-mail
            Transport.send(msg);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    private static String getEmailContent(String toAddress, String fullName, String code) {
        String activationLink = "http://localhost:8080/activeAccount?email=" + toAddress + "&code=" + code;
        String emailContent = "<html><body>" +
                "<p>Xin chào, " + fullName + "</p>" +
                "<p>Bạn vừa đăng ký tài khoản tại BookShopWeb Application. Để kích hoạt tài khoản, vui lòng nhấn vào liên kết bên dưới nếu không sẽ hết hạn sau 5 phút:</p>" +
                "<p><a href=\"" + activationLink + "\" style=\"text-decoration:none; color: #3498db; font-weight: bold;\">Kích hoạt</a></p>" +
                "<p>Nếu bạn không thực hiện đăng ký này, vui lòng bỏ qua thư này.</p>" +
                "<p>Trân trọng,</p>" +
                "<p>BookShopWeb Team</p>" +
                "</body></html>";
        return emailContent;
    }

    public static boolean isValidVerificationCode(String email, String code) {
        for (VerificationToken token : verificationTokenSet) {
            if (token.getEmail().equals(email)
                    && token.getCode().equals(code)
                    && System.currentTimeMillis() <= token.getExpirationTime()) {
                verificationTokenSet.remove(token);
                return true;
            }
        }
        return false;
    }
}
