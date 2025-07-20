package yps.systems.ai.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yps.systems.ai.model.Team;
import yps.systems.ai.object.TeamPerson;
import yps.systems.ai.repository.ITeamRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/teamService")
public class TeamController {

    private final ITeamRepository teamRepository;

    @Autowired
    public TeamController(ITeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @GetMapping
    ResponseEntity<List<Team>> getAll() {
        return ResponseEntity.ok(teamRepository.findAll());
    }

    @GetMapping("/{elementId}")
    ResponseEntity<Team> getByElementId(@PathVariable String elementId) {
        Optional<Team> teamOptional = teamRepository.findById(elementId);
        return teamOptional.map(team -> new ResponseEntity<>(team, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/getByLeaderElementId/{leaderElementId}")
    ResponseEntity<Team> getByLeaderElementId(@PathVariable String leaderElementId) {
        Team team = teamRepository.getByLeaderElementId(leaderElementId);
        if (team == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(team, HttpStatus.OK);
    }

    @GetMapping("/getByStudentElementId/{studentElementId}")
    ResponseEntity<Team> getByStudentElementId(@PathVariable String studentElementId) {
        Team team = teamRepository.getByStudentElementId(studentElementId);
        if (team == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(team, HttpStatus.OK);
    }

    @GetMapping("/getLeaderByTeamElementId/{teamElementId}")
    ResponseEntity<String> getLeaderByTeamElementId(@PathVariable String teamElementId) {
        String leaderElementId = teamRepository.getLeaderByTeamElementId(teamElementId);
        if (leaderElementId == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(leaderElementId, HttpStatus.OK);
    }

    @GetMapping("/getStudentsByTeamElementId/{teamElementId}")
    ResponseEntity<List<String>> getStudentsByTeamElementId(@PathVariable String teamElementId) {
        List<String> students = teamRepository.getStudentsByTeamElementId(teamElementId);
        if (students == null) {
            return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    @PostMapping
    ResponseEntity<String> save(@RequestBody Team team) {
        Team teamSaved = teamRepository.save(team);
        return new ResponseEntity<>("Team saved with ID: " + teamSaved.getElementId(), HttpStatus.CREATED);
    }

    @PostMapping("/setStudent")
    ResponseEntity<String> setStudentTo(@RequestBody TeamPerson teamPerson) {
        teamRepository.setStudentToTeam(teamPerson.teamElementId(), teamPerson.personElementId());
        return new ResponseEntity<>("Team related to student with ID: " + teamPerson.personElementId(), HttpStatus.CREATED);
    }

    @PostMapping("/setLeader")
    ResponseEntity<String> setLeaderTo(@RequestBody TeamPerson teamPerson) {
        teamRepository.setLeaderToTeam(teamPerson.teamElementId(), teamPerson.personElementId());
        return new ResponseEntity<>("Team related to leader with ID: " + teamPerson.personElementId(), HttpStatus.CREATED);
    }

    @DeleteMapping("/removeStudent")
    ResponseEntity<String> removeStudentFrom(@RequestBody TeamPerson teamPerson) {
        teamRepository.removeStudentFromTeam(teamPerson.teamElementId(), teamPerson.personElementId());
        return new ResponseEntity<>("Team remove related student with ID: " + teamPerson.personElementId(), HttpStatus.CREATED);
    }

    @DeleteMapping("/removeLeader")
    ResponseEntity<String> removeLeaderFrom(@RequestBody TeamPerson teamPerson) {
        teamRepository.removeLeaderFromTeam(teamPerson.teamElementId(), teamPerson.personElementId());
        return new ResponseEntity<>("Team related removed to leader with ID: " + teamPerson.personElementId(), HttpStatus.CREATED);
    }

    @DeleteMapping("/removePeople/{teamElementId}")
    ResponseEntity<String> removePeopleFrom(@PathVariable String teamElementId) {
        teamRepository.removePeopleFromTeam(teamElementId);
        return new ResponseEntity<>("Team related removed to people", HttpStatus.CREATED);
    }

    @DeleteMapping("/{elementId}")
    ResponseEntity<String> delete(@PathVariable String elementId) {
        Optional<Team> teamOptional = teamRepository.findById(elementId);
        if (teamOptional.isPresent()) {
            teamRepository.removePeopleFromTeam(teamOptional.get().getElementId());
            teamRepository.delete(teamOptional.get());
            return new ResponseEntity<>("Team deleted successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Team not founded", HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{elementId}")
    ResponseEntity<String> update(@PathVariable String elementId, @RequestBody Team team) {
        Optional<Team> teamOptional = teamRepository.findById(elementId);
        if (teamOptional.isPresent()) {
            team.setElementId(teamOptional.get().getElementId());
            teamRepository.save(team);
            return new ResponseEntity<>("Team updated successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Team not founded", HttpStatus.NOT_FOUND);
    }

}
