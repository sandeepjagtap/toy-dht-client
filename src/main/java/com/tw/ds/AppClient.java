package com.tw.ds;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.pattern.Patterns;
import com.typesafe.config.ConfigFactory;
import scala.concurrent.Await;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class AppClient {

    private List<ActorSelection> processes = new ArrayList<>();
    private Random random = new Random();
    private ActorRef deadLetters;

    private ActorSystem appClientSystem;

    public AppClient() {

        appClientSystem = ActorSystem.create("appClientSystem", ConfigFactory.load(("appclient")));
        deadLetters = appClientSystem.deadLetters();

        ActorSelection process1 = appClientSystem.actorSelection("akka.tcp://appSystem@127.0.0.1:2557/user/appActor");
        ActorSelection process2 = appClientSystem.actorSelection("akka.tcp://appSystem@127.0.0.1:2558/user/appActor");
        ActorSelection process3 = appClientSystem.actorSelection("akka.tcp://appSystem@127.0.0.1:2559/user/appActor");
        ActorSelection process4 = appClientSystem.actorSelection("akka.tcp://appSystem@127.0.0.1:2560/user/appActor");
        processes.add(process1);
        processes.add(process2);
        processes.add(process3);
        processes.add(process4);
    }

    public AppClient(String actorPath, String fileName) {
        appClientSystem = ActorSystem.create("appClientSystem", ConfigFactory.load((fileName)));
        deadLetters = appClientSystem.deadLetters();
        ActorSelection process = appClientSystem.actorSelection(actorPath);
        processes.add(process);

    }




    public void put(int key, int value) {
        ActorSelection process = processes.get(random.nextInt(processes.size()));
        process.tell(new PutMessage(key, value), deadLetters);
    }

    public int get(int key) throws Exception {
        ActorSelection process = processes.get(random.nextInt(processes.size()));
        FiniteDuration tenSeconds = Duration.apply(10, TimeUnit.SECONDS);
        Object result = Await.result(Patterns.ask(process,  new ReadMessage(key), tenSeconds.toMillis()), tenSeconds);
        return ((ReadMessageAnswer) result).getValue();
    }

    public void shutdown() {
        appClientSystem.shutdown();
    }
}
