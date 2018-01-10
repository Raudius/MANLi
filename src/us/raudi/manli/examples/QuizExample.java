package us.raudi.manli.examples;

import java.io.IOException;

import us.raudi.manli.Amend;
import us.raudi.manli.ManliClient;
import us.raudi.manli.ManliServer;

public class QuizExample {
	public static void main(String[] args) throws IOException, InstantiationException, IllegalAccessException {
		
		// Start model
		SimpleQuiz quiz = new SimpleQuiz();
		quiz.start();
		
		// Give model to server
		ManliServer host = new ManliServer(quiz);
		new Thread(host).start();
		
		// give time for server thread to initiate
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// For each name in the array create a connection.
		String[] names = {"Alice", "Bob", "Charlie"};
				
		for(int i=0; i<names.length; i++) {
			ManliClient c = new ManliClient("localhost", host.getPort());
			c.start(SimpleQuiz.class);
			
			Amend a = new Amend();
			a.putString("name", names[i]);
			// register name
			c.sendAmend(a);
			
			if(i == 2) {
				a.putBool("buzz", true);
				
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				

				c.sendAmend(a);
				
				
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				c.disconnect();
			}
		}
		
	}
}
