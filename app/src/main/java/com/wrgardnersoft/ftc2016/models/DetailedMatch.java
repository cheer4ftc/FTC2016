package com.wrgardnersoft.ftc2016.models;

/**
 * Created by Bill on 2/11/2015.
 */
public class DetailedMatch {

    public Match m[];

    public DetailedMatch() {
        this.m = new Match[MyApp.TEAMS_PER_ALLIANCE];
        for (int i=0; i<MyApp.TEAMS_PER_ALLIANCE; i++) {
            this.m[i]=new Match();
        }
    }

    public DetailedMatch(Match m[]) {
        this.m = new Match[MyApp.TEAMS_PER_ALLIANCE];
        for (int i=0; i<MyApp.TEAMS_PER_ALLIANCE; i++) {
            this.m[i]=new Match(m[i]);
        }
    }

}
