import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;

//Will not work until ConnectorJ Driver is in classpath
public Connection getConnection() throws SQLException{
	Connection c = null;
	Properties properties = new Properties();
	properties.put("user", this.userName);
	properties.put("password", this.password);
	c = DriverManager.getConnection("jdbc:mysql://localhost:3306/pastpapers", properties);
	System.out.println("Connected to database");
	return conn;
}

public class PhysicsQuiz extends Frame implements WindowListener, ActionListener{
	private Label menuheader;
	private Choice topiclist;
	private Button test;
	private Button scores;
	private Button quit;
	private Label questionheader;
	private Choice multiquestion;
	private TextField calculation;
	private TextField equation;
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
	public void Test(){
		String topic = topiclist.getSelectedItem();
		remove(topiclist);
		remove(test);
		remove(scores);
		remove(quit);
		
		question[] questions = new question[10];

		for(int j;j<questions.length;j++){
			Connection c = createConnection();
			Statement st = c.createStatement();
			int random = Rand.nextInt();
			String sqlcontent = "SELECT Content FROM question WHERE Topic = " + topic "AND QuestionID = " + random;
			ResultSet rs1 = st.executeQuery(sqlcontent);
			ResultSet rs1 = st.getResultSet();
			try{
				while (rs1.next()){
					questions[j].content = rs1.getString(1);		
				}
			} finally {
				rs1.close;
			}
			String sqltype = "SELECT Type FROM question WHERE QuestionID = " + random;
			ResultSet rs2 = st.executeQuery(sqltype);
			ResultSet rs2 = st.getResultSet();
			try{
				while (rs2.next()){
					questions[j].type = rs2.getString(1);		
				}
			} finally {
				rs2.close;
			}
			st.close();
			c.close();
		}

		for(int i = 0;i<questions.length;i++){
			if(questions[i].type == "Multiple Choice"){
				questionheader = new Label("Question " + i + " : " + questions[i].content);
				multiquestion = new Choice();
			}	
			if(questions[i].type == "Calculation"){
				questionheader = new Label("Question " + i + " : " + questions[i].content);
				calculation = new TextField();
				equation = new TextField();
			}
		}
		
	}
	public static void main(String[] args){
		PhysicsQuiz app = new PhysicsQuiz();
	}
	public void actionPerformed(ActionEvent e){
		String action = e.getActionCommand();
		if(action.equals("Test!")){
			Test();
		}
		if(action.equals("View previous scores on this topic")){
		
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
