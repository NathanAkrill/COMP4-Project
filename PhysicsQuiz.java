import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import java.util.List;
import javax.swing.*;

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
	private JTextField calculation;
	private JTextField equation;
	private boolean answered = false;
	String topic;
	List<Integer> primaryKeys = new LinkedList<Integer>();
	int questionNo = 0;
	int score = 0;
	question[] questions = new question[10];
	String[] answers = new String[10];
	String[] multianswers = new String[10];
	String[] calcanswers = new String[10];
	String[] eqanswers = new String[10];
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
	public void getTopicQuestions() throws SQLException{
		try{
			Connection c = getConnection();
			Statement st = c.createStatement();
			String sqlcontent = "SELECT QuestionID FROM question WHERE QuestionTopic = '" + topic + "'";
			ResultSet rs0 = st.executeQuery(sqlcontent);
			while(rs0.next()){
				System.out.println(rs0.getInt(1));
				primaryKeys.add(rs0.getInt(1));
			}
		}catch(SQLException ex){
			System.out.println(ex);
		}
	}
	public void getTopicAnswers() throws SQLException{
	for(int f=0;f<questions.length;f++){
		try{
			Connection c = getConnection();
			Statement st = c.createStatement();
			String sqlcontent = "SELECT AnswerContent FROM answer WHERE AnswerID = '" + questions[f].id + "'";
			ResultSet rsA = st.executeQuery(sqlcontent);
			try{
				while (rsA.next()){
					answers[f] = rsA.getString(1);
				}
			} finally {
				rsA.close();
			}
		}catch(SQLException ex){
			System.out.println(ex);
		}
	}
	}
	public void displayNextQuestion(){
		if(questions[questionNo].type.equals("Multiple Choice")){
				questionheader = new Label("Question " + questionNo + " : " + questions[questionNo].content);
				add(questionheader);
				multiquestion = new Choice();
				add(multiquestion);
				submit = new Button("Submit");
				add(submit);
				submit.addActionListener(this);
				setVisible(true);
				repaint();
		}
			else if(questions[questionNo].type.equals("Calculation")){
				questionheader = new Label("Question " + questionNo + " : " + questions[questionNo].content);
				add(questionheader);
				calculation = new JTextField(20);
				add(calculation);
				equation = new JTextField(20);
				add(equation);
				//add JTextField for Units.
				submit = new Button("Submit");
				add(submit);
				submit.addActionListener(this);
				setVisible(true);
				repaint();
			}
			else{
				System.out.println("Error. Cannot get results from query");
				System.out.println(questions[questionNo].content);
				//Question about pigeons is broken.
			}
	}
	public void Answers(){
		if(questions[questionNo].type.equals("Multiple Choice")){
			multianswers[questionNo] = multiquestion.getSelectedItem();
			if(multianswers[questionNo].equals(answers[questionNo])){
				score++;
			}
		}
		else if(questions[questionNo].type.equals("Calculation")){
			calcanswers[questionNo] = calculation.getText();
			eqanswers[questionNo] = equation.getText();
			if(calcanswers[questionNo].equals(answers[questionNo])){
				score++;
			}
			if(eqanswers[questionNo].equals(answers[questionNo])){
				score++;
			}
		}
		else{
			System.out.println("Error. Cannot get results from query");
			System.out.println(questions[questionNo].content);
			//Question about pigeons is broken.
		}
	}
	public void Test() throws SQLException{
		remove(topiclist);
		remove(test);
		remove(scores);
		remove(quit);
		remove(menuheader);
		
		
		for(int k=0;k<questions.length;k++){
			questions[k] = new question();
		}

		for(int j=0;j<questions.length;j++){
			try{
				Connection c = getConnection();
				Statement st = c.createStatement();
				int random = rand.nextInt(primaryKeys.size());
				questions[j].id = primaryKeys.get(random);
				//System.out.println(random);
				String sqlcontent = "SELECT QuestionContent FROM question WHERE QuestionID = " + primaryKeys.get(random);
				ResultSet rs1 = st.executeQuery(sqlcontent);
				try{
					while (rs1.next()){
						questions[j].content = rs1.getString(1);
						//System.out.println(questions[j].content);
					}
				} finally {
					rs1.close();
				}
				String sqltype = "SELECT QuestionType FROM question WHERE QuestionID = " + primaryKeys.get(random);
				ResultSet rs2 = st.executeQuery(sqltype);
				try{
					while (rs2.next()){
						questions[j].type = rs2.getString(1);	
						//System.out.println(questions[j].type);
					}
				} finally {
					rs2.close();
				}
				primaryKeys.remove(random);
				st.close();
				c.close();
			}catch(SQLException ex){
				System.out.println(ex);
			}
		}
		displayNextQuestion();
		
	}
	public static void main(String[] args){
		PhysicsQuiz app = new PhysicsQuiz();
	}
	public void actionPerformed(ActionEvent e){
		String action = e.getActionCommand();
		if(action.equals("Test!")){
			topic = topiclist.getSelectedItem();

			try{
				getTopicQuestions();
				getTopicAnswers();
				Test();
			}
			catch(SQLException ex){
				System.out.println(ex);
			}
		}
		if(action.equals("View previous scores on this topic")){
		
		}
		if(action.equals("Submit")){
			Answers();
			
			this.removeAll();
			answered = true;
			questionNo++;
			if(questionNo < questions.length){
				displayNextQuestion();
			}
			else{
				
			}
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
