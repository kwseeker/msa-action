package top.kwseeker.msa.action.webbdcc.domain.activity.model;

public interface Activity {

    String getId();

    String getName();

    String getStartTime();

    String getEndTime();

    ActivityState state();

    String info();
}
