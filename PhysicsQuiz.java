import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;

public class PhysicsQuiz extends Frame implements WindowListener, ActionListener{
	
	public Connection getConnection() throws SQLException{
		String user  = "root";
		String pass = "usbw";
		Connection c = null;
		c = DriverManager.getConnection("jdbc:mysql://localhost:3307/pastpapers", user, pass);
		System.out.println("Connected to database");
		return c;
	}
	private Random rand = new Random();
	private Label menuheader;
	private Choice topiclist;
	private Button test;
	private Button scores;
	private Button quit;
	private Button submit;
	private Label questionheader;
	private Choice multiquestion;
	private TextField calculation;
	private TextField equation;
	private boolean answered = false;
	public PhysicsQuiz(){
		setLayout(new FlowLayout());
		menuheader = new Label("Choose the topic you wish to revise.");
		add(menuheader);
		topiclist = new Choice();
		topiclist.add("Mechanics");
		topiclist.add("Materials");
		topiclist.add("Waves");
		topiclist.add("DC Electricity");
		topiclist.add("Nature of Light");
		add(topiclist);
		test = new Button("Test!");
		add(test);
		test.addActionListener(this);
		scores = new Button("View previous scores on this topic");
		add(scores);
		scores.addActionListener(this);
		quit = new Button("Quit");
		add(quit);
		quit.addActionListener(this);
		addWindowListener(this);
		setTitle("Physics Quiz");
		setSize(250, 300);
		setVisible(true);
	}
	public void Test() throws SQLException{
		String topic = topiclist.getSelectedItem();
		remove(topiclist);
		remove(test);
		remove(scores);
		remove(quit);
		remove(menuheader);
		
		question[] questions = new question[10];
		for(int k=0;k<questions.length;k++){
			questions[k] = new question();
		}

		for(int j=0;j<questions.length;j++){
			try{
				Connection c = getConnection();
				Statement st = c.createStatement();
				int random = rand.nextInt(33);
				System.out.println(random);
				String sqlcontent = "SELECT QuestionContent FROM question WHERE QuestionTopic = '" + topic + "' AND QuestionID = " + random;
				ResultSet rs1 = st.executeQuery(sqlcontent);
				try{
					while (rs1.next()){
						questions[j].content = rs1.getString(1);
						System.out.println(questions[j].content);
					}
				} finally {
					rs1.close();
				}
				String sqltype = "SELECT QuestionType FROM question WHERE QuestionTopic = '" + topic + "' AND QuestionID = " + random;
				ResultSet rs2 = st.executeQuery(sqltype);
				try{
					while (rs2.next()){
						questions[j].type = rs2.getString(1);	
						System.out.println(questions[j].type);
					}
				} finally {
					rs2.close();
				}
				st.close();
				c.close();
			}catch(SQLException ex){
				System.out.println(ex);
			}
		}

		for(int i = 0;i<questions.length;i++){
			while(answered = false){
				if(questions[i].type.equals("Multiple Choice")){
					questionheader = new Label("Question " + i + " : " + questions[i].content);
					add(questionheader);
					multiquestion = new Choice();
					add(multiquestion);
					submit = new Button("Submit");
					add(submit);
					submit.addActionListener(this);
					setVisible(true);
					repaint();
				}
				if(questions[i].type.equals("Calculation")){
					questionheader = new Label("Question " + i + " : " + questions[i].content);
					add(questionheader);
					calculation = new TextField("Enter your answer");
					add(calculation);
					equation = new TextField("Enter the equation");
					add(equation);
					submit = new Button("Submit");
					add(submit);
					submit.addActionListener(this);
					setVisible(true);
					repaint();
				}
				else{
					System.out.println("Error. Cannot get results from query");
				}
			}
		}
		
	}
	public static void main(String[] args){
		PhysicsQuiz app = new PhysicsQuiz();
	}
	public void actionPerformed(ActionEvent e){
		String action = e.getActionCommand();
		if(action.equals("Test!")){
			try{
				Test();
			}
			catch(SQLException ex){
				System.out.println(ex);
			}
		}
		if(action.equals("View previous scores on this topic")){
		
		}
		if(action.equals("Submit")){
			answered = true;
		}
		if(action.equals("Quit")){
			System.exit(0);
		}
	}
    public void windowClosing(WindowEvent e) {
		System.exit(0);
	}
	public void windowOpened(WindowEvent e) { }
	public void windowClosed(WindowEvent e) { }
	public void windowIconified(WindowEvent e) { }
	public void windowDeiconified(WindowEvent e) { }
	public void windowActivated(WindowEvent e) { }
	public void windowDeactivated(WindowEvent e) { }
}
