Welcome to oogway.
-------------

*Background music while you're reading: https://www.youtube.com/watch?v=7LHYBHUsn2M*


In my conviction, each computer scientist with a basic sense of philosophy and a respect for the history of scientific understanding, must at least once in his lifetime read COMPUTING MACHINERY AND INTELLIGENCE on The Imitation Game (https://www.csee.umbc.edu/courses/471/papers/turing.pdf) and set out to build his/her own Turing Machine. Simple it may be; blended with ones personal taste of aesthetics, humor and technology you can rise above yourself and design your own piece of artificial intelligence. For no particular reason, simply because you can.

Not doing so is like a mathematician who, having the intellectual tool set to do so, has never created his own proof of Pythagoras' theorem on right triangles. Or an engineer in robotics who in his humble life has never built his own robot. A physicist without a prism at home, or one who has never at least attempted to find a flaw in Einstein's theory of relativity. An astronomer not seeking an unknown star through his old dusty telescope, a mountaineer who has never set foot on a Himalayan peak, an Italian who does not know how to make a pizza, a triathlete who has not finished Hawaï's legendary race, a Muslim who has always dreamed of Mecca but never fulfilled his religious duty of Hajj, a talented yet unfortunate violin player who never played the 'Ode to Joy', a yogi afraid to do Sirsasana, etcetera, etcetera. You get the point. You miss a great chance. A great opportunity. I say unto you: your life will not be complete before you take up the challenge. You have the skill set but fail to comprehend the main historical barriers mankind has taken in your particular field of interest. And how much fun it is to step in the food steps of our genius ancestors!!!

Thus, engineers, computer scientists, software developers, programmers and hackers, please check out this code and run this modest Turing Machine. Play with it, adapt it, and change it to suit your needs. Modest, as it is bound to fail the original test. Yet, fortunately, it was good fun building it.

Installation guide
------------------

ElasticSearch is used to create a simple knowledge base with (borrowed) wisdom, referred to here as 'the Oracle'. Go here https://www.elastic.co/ and install ElasticSearch on your computer. I'm using version 2.4.4 and it runs fine. Define a cluster named 'delphi'. To simply test it you can leave default values for all other configurations.

Through RabbitMQ the different micro-services communicate. Go here https://www.rabbitmq.com/ and install RabbitMQ. Version 3.6.10. should do. No need to configure anything. Just run it.

At last, for playing with logs also install logstash. Adapt and run run-env.bat to start up the just mentioned tooling in one go.

Now check out this code. It being a micro-services architecture, you need to run OrchestrationServer, SannyasServer and WebServer. Easiest, for Windows users at least, is to run the script build-and-run.bat. It does what the title suggests. Load balancing on multiple running instances of the SannyasServer and/or of the WebServer are dealt with by the OrchestrationServer. 

*Disclaimer* :) 
Inspecting the project you will notice strange or at least uncommon words throughout the code ('Sannyasin' instead of 'Worker', 'Manager' instead of 'WorkerEngine'). This being a hobby project, I have used the lack of supervision to take a few coding short cuts, and have been excessively creative in choosing class, method and variable names. Don't try this at home when you're creating real life production software and please stick to the commonly used patterns and naming conventions to ease your colleagues' professional life.

I am grateful to people who post informative code examples on GitHub, to the ones who write useful blog posts, those who help others to transcend their technical struggles through stackoverflow, and of course the whole community of countless anonymous living beings working diligently on great and handy open source projects.

If you have any ideas or suggestions for future improvements, or problems running this software, please comment and I will try to add new features. In case you want to personally contribute to the code, that of course is possible too.

Enough for today. Thank you for reading this and have a nice day.

***Namaste***

----------

> *For instance, on the planet Earth, man had always assumed that he was more intelligent than dolphins because he had achieved so much—the wheel, New York, wars and so on—whilst all the dolphins had ever done was muck about in the water having a good time. But conversely, the dolphins had always believed that they were far more intelligent than man—for precisely the same reasons.* ― Douglas Adams, The Hitchhiker's Guide to the Galaxy


