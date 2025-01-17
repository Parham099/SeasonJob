# How Depend?
Add Jar file to src folder and use this in pom.xml
```xml
        <dependency>
            <groupId>ir.parham</groupId>
            <artifactId>seasonjob</artifactId>
            <version>1.0</version>
            <scope>system</scope>
            <systemPath>${project.basedir}/src/SeasonJobs-New-1.0-SNAPSHOT.jar</systemPath>
        </dependency>
```

# SeasonJobs-Api

Support SeasonJob Plugin After 1.1.1 Version!


load plugin with LoadJobApi live:

# Jobs Method:
**Kotlin**:
```kt
        val job = Job();
        
        job.save("Job Name") // Save Job
        job.saveAll() // Save All Jobs
        
        job.loadAll() // Load All Jobs
        
        job.contains("Job Name") // Job Exits Check
        job.list() // Get Jobs Name
        
        job.create("Job Name", members_size, need_playtime, "Job Prefix", "Job Suffix") // Create Job
        job.remove("Job Name") // Remove Job
        
        job.get("Job Name")?.Name // Job Name
        job.get("Job Name")?.Prefix // Job Prefix
        job.get("Job Name")?.Suffix // Job Suffix
        job.get("Job Name")?.PlayTime // Job Paytime For Join
        job.get("Job Name")?.MemberSize // Max Members Size
```
**Java**:
```java
        Job job = new Job();
        
        job.save("Job Name"); // Save Job
        job.saveAll(); // Save All Jobs
        
        job.loadAll(); // Load All Jobs
        
        job.contains("Job Name"); // Job Exits Check
        job.list(); // Get Jobs Name
        
        job.create("Job Name", members_size, need_playtime, "Job Prefix", "Job Suffix"); // Create Job
        job.remove("Job Name"); // Remove Job
        
        job.get("Job Name").getName; // Job Name
        job.get("Job Name").getPrefix; // Job Prefix
        job.get("Job Name").getSuffix; // Job Suffix
        job.get("Job Name").getPlayTime; // Job Paytime For Join
        job.get("Job Name").getMemberSize; // Max Members Size
```

# Member Methods
**Kotlin**:
```kt
        val member = Member();
        
        member.save(UUID) // Save A Member Data
        member.saveAll() // Save All Members Data
        member.loadAll() // load all members
        
        member.list() // Workers UUIDs
        member.getJobMembers("Job Name") // Get a one job members
        
        member.add(UUID, "Job Name") // Add Player To Members
        member.remove(UUID) // remove members from list
        member.set(newUUID, newWarn, newPlaytime, newJob) // Change And Set Values Of A Member
        
        member.contains(UUID) // Player Is A Worker?
        
        member.get(UUID)?.UUID // member uuid
        member.get(UUID)?.JobName // member job
        member.get(UUID)?.Warns // member warns
        member.get(UUID)?.PlayTime // member playtime
```
**Java**:
```java
        Member member = new Member();
        
        member.save(UUID) // Save A Member Data
        member.saveAll() // Save All Members Data
        member.loadAll() // load all members
        
        member.list() // Workers UUIDs
        member.getJobMembers("Job Name") // Get a one job members
        
        member.add(UUID, "Job Name") // Add Player To Members
        member.remove(UUID) // remove members from list
        member.set(newUUID, newWarn, newPlaytime, newJob) // Change And Set Values Of A Member
        
        member.contains(UUID) // Player Is A Worker?
        
        member.get(UUID).getUUID // member uuid
        member.get(UUID).getJobName // member job
        member.get(UUID).getWarns // member warns
        member.get(UUID).getPlayTime // member playtime
        member.get(UUID).addMinPlaytime // add 1min to player playtime
```
