import java.awt.*;
import java.awt.event.*;

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

		//for(int j;j<questions.length;j++){
		//	int random = Rand.nextInt
		//	questions[j].content = (
		//	SELECT Content
		//	FROM question
		//	WHERE Topic = topic AND QuestionID = random
		//	)
		//	
		//	questions[j].type = (
		//	SELECT Type
		//	FROM question
		//	WHERE QuestionID = random
		//	)

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