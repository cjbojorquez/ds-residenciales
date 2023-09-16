package gt.com.ds.util;

import java.io.File;

/**
 *
 * @author cjbojorquez
 */
public interface EmailService {

    public void sendSimpleMessage(String to, String subject, String text,String origin);
    
    public void sendMessage(String to, String subject, String text,File file, String origin);
}
