import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import javax.mail.*;
import java.util.Arrays;
import java.util.Properties;

public class Credentials {
    private String databaseIPAddress;
    private String databasePassword;
    private String databasePort;
    private String databaseSchema;
    private String databaseUserName;
    private String emailAddress;
    private String emailPassword;
    private QueryMaker queryMaker;

    /**
     * Constructor for credentials login
     */

    public Credentials() {
        new Login(this);
    }

    /**
     * @return a new queryMaker
     * @throws SQLException
     * @throws ClassNotFoundException
     */

    public QueryMaker getQueryMaker() throws SQLException, ClassNotFoundException {
        if (queryMaker != null)
            return queryMaker;
        else
            return queryMaker = new QueryMaker(databaseUserName, databasePassword, databaseIPAddress, databasePort, databaseSchema);
    }

    /**
     * @return email address
     */

    public String getEmail() {

        return emailAddress;
    }

    /**
     * @return emails password
     */

    public String getEmailPassword() {
        return emailPassword;
    }

    private String getField(JComponent field) {

        if (field instanceof JTextField) {
            return ((JTextField) field).getText();
        } else if (field instanceof JPasswordField) {
            JPasswordField passwordField = (JPasswordField) field;
            return String.join("", Arrays
                    .toString(passwordField
                            .getPassword()));
        }
        return null;
    }

    /**
     * @param session
     * @return the inbox given the email login credentials using a new session
     * @throws MessagingException
     */

    public Message[] getMessages(Session session)
            throws MessagingException {

        Properties pro = System.getProperties();
        pro.setProperty("mail.store.protocol", "imaps");
        try {
            Store store = session.getStore("imaps");
            store
                    .connect("imap.gmail.com", emailAddress, emailPassword);
            Folder inbox = store.getFolder("Inbox");
            inbox.open(Folder.READ_WRITE);
            return inbox.getMessages();
        } catch (MessagingException e) {
            throw new MessagingException();
        }
    }

    /**
     * @return Using the java mail it will connection to the Gmail smtp server
     */

    public Session getSession() {

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        return Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication(emailAddress, emailPassword);
            }
        });
    }

    /**
     * Creates string array of credentials for login
     */

    private class Login {
        String[] fields =
                new String[]{
                        "Database User Name",
                        "Database Password",
                        "Database IP Address",
                        "Database Port",
                        "Database Schema",
                        "Email Address",
                        "Email Password"
                };
        JFrame loginFrame = new JFrame("Credentials");

        /**
         * Creates a user friendly interface for logging into the database and Gmail accounts simultaneously
         * This login is required for every debug/ run
         * This eliminates security risks by having the credentials stored only on local machines
         *
         * @param credentials The GUI interface takes on the credentials argument which allocates where the credentials belong
         */

        public Login(Credentials credentials) {
            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints constraints = new GridBagConstraints();
            JTextField databaseUserNameField = new JTextField(20);
            JPasswordField databasePasswordField = new JPasswordField(20);
            JTextField databaseIPAddressField = new JTextField(20);
            JTextField databasePortField = new JTextField(20);
            JTextField databaseSchemaField = new JTextField(20);
            JTextField emailAddressField = new JTextField(20);
            JPasswordField emailPasswordField = new JPasswordField(20);
            JLabel[] labels = new JLabel[fields.length];
            JButton button = new JButton("Connect");

            JComponent[] components = {
                    databaseUserNameField, databasePasswordField,
                    databaseIPAddressField, databasePortField,
                    databaseSchemaField, emailAddressField, emailPasswordField
            };
            int i = 0;
            for (; i < components.length; i++) {
                // add label
                constraints.gridy = i;
                panel.add(new JLabel(fields[i]), constraints);
                panel.add(components[i], constraints);
            }
            constraints.gridx++;
            constraints.gridy++;
            panel.add(button, constraints);


            loginFrame.getContentPane().add(panel);
            loginFrame.pack();
            loginFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            loginFrame.setLocationRelativeTo(null);
            loginFrame.setVisible(true);
            button.addActionListener(e -> {
                try {
                    queryMaker =
                            new QueryMaker(
                                    databaseUserNameField.getText(),
                                    String.valueOf(databasePasswordField.getPassword()),
                                    databaseIPAddressField.getText(),
                                    databasePortField.getText(),
                                    databaseSchemaField.getText()
                            );
                    emailAddress = emailAddressField.getText();
                    emailPassword = String.valueOf(emailPasswordField.getPassword());

                    new Main().invoke(credentials, queryMaker);
                } catch (SQLException | ClassNotFoundException | FileNotFoundException throwables) {
                    throwables.printStackTrace();
                    queryMaker = null;
                    JOptionPane.showMessageDialog(
                            null, "You have not signed in yet");
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            });
        }
    }
}