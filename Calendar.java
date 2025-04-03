package DATE;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.BoxLayout;
import javax.swing.border.EmptyBorder;
import com.toedter.calendar.JDateChooser;
import java.util.Date;
import java.awt.Color;
import java.awt.Dimension;
import org.eclipse.wb.swing.FocusTraversalOnArray;
import java.awt.Component;
import com.toedter.calendar.JYearChooser;

public class Calendar extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Calendar frame = new Calendar();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Calendar() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(300, 300, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 100, 5, 0));
		setContentPane(contentPane);
		
		// Use BoxLayout to arrange components vertically
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

		// Create labels for check-in and check-out dates
		JLabel lblCheckIn = new JLabel("Check-in Date:");
		JLabel lblCheckOut = new JLabel("Check-out Date:");

		// Create date choosers for check-in and check-out
		JDateChooser checkInDateChooser = new JDateChooser();
		checkInDateChooser.setToolTipText("he");
		checkInDateChooser.setBackground(new Color(255, 0, 0));
		checkInDateChooser.setDateFormatString("yyyy-MM-dd");  // Optional: format the date as yyyy-MM-dd
		checkInDateChooser.setPreferredSize(new Dimension(4, 1));  // Increased size of the date box
		checkInDateChooser.setFont(new Font("Arial", Font.PLAIN, 12));
		
		JDateChooser checkOutDateChooser = new JDateChooser();
		checkOutDateChooser.setBackground(new Color(0, 255, 0));
		checkOutDateChooser.setDateFormatString("yyyy-MM-dd");  // Optional: format the date as yyyy-MM-dd
		checkOutDateChooser.setPreferredSize(new java.awt.Dimension(10, 1));  // Increased size of the date box
		checkInDateChooser.setFont(new Font("Arial", Font.PLAIN, 12));
		// Create a button to validate the dates
		JButton btnCheckDates = new JButton("Check Dates");

		// Add ActionListener for the button
		btnCheckDates.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Get the selected dates
				Date checkInDate = checkInDateChooser.getDate();
				Date checkOutDate = checkOutDateChooser.getDate();
				Date currentDate = new Date();  // Get the current date

				// Check if either date is null (user didn't select a date)
				if (checkInDate == null || checkOutDate == null) {
					JOptionPane.showMessageDialog(null, "Please select both check-in and check-out dates.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}

				// Check if the dates are in the past
				if (checkInDate.before(currentDate) || checkOutDate.before(currentDate)) {
					JOptionPane.showMessageDialog(null, "Please choose valid dates. Dates cannot be in the past.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}

				// Check if check-out date is before or equal to check-in date
				if (checkOutDate.before(checkInDate) || checkOutDate.equals(checkInDate)) {
					JOptionPane.showMessageDialog(null, "Check-out date must be after the check-in date.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}

				// If validation passes
				JOptionPane.showMessageDialog(null, "Dates are valid.", "Success", JOptionPane.INFORMATION_MESSAGE);
			}
		});

		// Add components to content pane
		contentPane.add(lblCheckIn);
		contentPane.add(checkInDateChooser);
		checkInDateChooser.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{checkInDateChooser.getCalendarButton()}));
		contentPane.add(lblCheckOut);
		contentPane.add(checkOutDateChooser);
		contentPane.add(btnCheckDates);
	}
}
