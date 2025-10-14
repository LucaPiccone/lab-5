package usecase;

import api.GradeDataBase;
import api.MongoGradeDataBase;
import entity.Grade;
import entity.Team;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

import java.io.IOException;

/**
 * GetAverageGradeUseCase class.
 */
public final class GetAverageGradeUseCase {
    private final GradeDataBase gradeDataBase;

    public GetAverageGradeUseCase(GradeDataBase gradeDataBase) {
        this.gradeDataBase = gradeDataBase;
    }

    /**
     * Get the average grade for a course across your team.
     * @param course The course.
     * @return The average grade.
     */
    public float getAverageGrade(String course) throws IOException {
        // Call the API to get usernames of all your team members
        float sum = 0f;
        int count = 0;
        // TODO Task 3b: Go to the MongoGradeDataBase class and implement getMyTeam.
        final Team team = gradeDataBase.getMyTeam();
        if (team == null || team.getMembers() == null || team.getMembers().length == 0) {
            return 0f;
        }
        for (String member : team.getMembers()) {
            if (member == null || member.isEmpty()) continue;

            // May throw at runtime from DB impl; we let RuntimeException bubble up,
            // since this method already declares IOException and caller shows errors via UI.
            final Grade[] grades = gradeDataBase.getGrades(member);
            if (grades == null) continue;

            // Find the (first) matching course grade (case-insensitive)
            for (Grade g : grades) {
                if (g != null && g.getCourse() != null
                        && g.getCourse().equalsIgnoreCase(course)) {
                    sum += g.getGrade(); // int -> added to float
                    count++;
                    break; // assume one grade per course per user; stop after first match
                }
            }
        }
        return count == 0 ? 0f : (sum / count);

        // Call the API to get all the grades for the course for all your team members
        // TODO Task 3a: Complete the logic of calculating the average course grade for
        //              your team members. Hint: the getGrades method might be useful.
    }
}
