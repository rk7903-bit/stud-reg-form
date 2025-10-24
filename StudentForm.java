import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class StudentForm extends JFrame {
    private JTextField txtName, txtEmail, txtCourse, txtPhone;
    private JTable table;
    private DefaultTableModel model;
    private int selectedId = -1;

    public StudentForm() {
        setTitle("Student Registration");
        setSize(700, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        txtName = new JTextField();
        txtEmail = new JTextField();
        txtCourse = new JTextField();
        txtPhone = new JTextField();

        formPanel.add(new JLabel("Name"));
        formPanel.add(txtName);
        formPanel.add(new JLabel("Email"));
        formPanel.add(txtEmail);
        formPanel.add(new JLabel("Course"));
        formPanel.add(txtCourse);
        formPanel.add(new JLabel("Phone"));
        formPanel.add(txtPhone);

        JButton btnAdd = new JButton("Add");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Delete");

        JPanel btnPanel = new JPanel();
        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);

        add(formPanel, BorderLayout.NORTH);
        add(btnPanel, BorderLayout.CENTER);

        model = new DefaultTableModel(new Object[]{"ID", "Name", "Email", "Course", "Phone"}, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.SOUTH);

        loadTable();

        btnAdd.addActionListener(e -> addStudent());
        btnUpdate.addActionListener(e -> updateStudent());
        btnDelete.addActionListener(e -> deleteStudent());

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int i = table.getSelectedRow();
                if (i >= 0) {
                    selectedId = Integer.parseInt(model.getValueAt(i, 0).toString());
                    txtName.setText(model.getValueAt(i, 1).toString());
                    txtEmail.setText(model.getValueAt(i, 2).toString());
                    txtCourse.setText(model.getValueAt(i, 3).toString());
                    txtPhone.setText(model.getValueAt(i, 4).toString());
                }
            }
        });
    }

    private void loadTable() {
        model.setRowCount(0);
        List<Student> list = StudentDAO.getAllStudents();
        for (Student s : list) {
            model.addRow(new Object[]{s.getId(), s.getName(), s.getEmail(), s.getCourse(), s.getPhone()});
        }
    }

    private void addStudent() {
        if (txtName.getText().isEmpty() || txtEmail.getText().isEmpty()) return;
        Student s = new Student(txtName.getText(), txtEmail.getText(), txtCourse.getText(), txtPhone.getText());
        StudentDAO.addStudent(s);
        loadTable();
        clearForm();
    }

    private void updateStudent() {
        if (selectedId == -1) return;
        Student s = new Student(selectedId, txtName.getText(), txtEmail.getText(), txtCourse.getText(), txtPhone.getText());
        StudentDAO.updateStudent(s);
        loadTable();
        clearForm();
    }

    private void deleteStudent() {
        if (selectedId == -1) return;
        StudentDAO.deleteStudent(selectedId);
        loadTable();
        clearForm();
    }

    private void clearForm() {
        txtName.setText("");
        txtEmail.setText("");
        txtCourse.setText("");
        txtPhone.setText("");
        selectedId = -1;
    }
}
