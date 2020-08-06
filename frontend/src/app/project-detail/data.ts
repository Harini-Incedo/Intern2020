import { Skill } from '../Classes/skill';

export let skills: Skill[] = [
  {
    id: 1,
    "skillName" : "Java",
    "projectName": "",
    "count": 2,
    "totalWeeklyHours" : 80,
  },
  // {
  //   "id": 2,
  //   "skillName" : "Python",
  //   "totalHours" : 40,
  //   "projectName": "",
  //   "peopleNeeded": 1,
  // }
]

skills[0]["engagements"] = [{
 engagement: {
  assignedWeeklyHours:{"01/07/19": 10, "01/14/19": 10},
  startDate:"01/07/2019",
  endDate:"01/21/2020",
 },
 employee: {
  "id": 3,
   fn: "John", ls:"Doe"
  }},
  {
    "engagement": {
        "id": 14,
        "employeeID": 3,
        "projectID": 9,
        "skillID": 1,
        "startDate": "2020-06-01",
        "endDate": "2020-07-01",
        "hoursNeeded": 40,
        "assignedWeeklyHours": {
          "2019-01-07": 10,
          "2019-01-14": 20,
      }
  },
  "employee": {
      "id": 3,
      "fn": "Jennifer",
      "ls": "Aniston",
      "email": "jennifer@domain.com",
      "startDate": "2019-01-01",
      "endDate": "2020-01-01",
      "active": true,
      "workingHours": "Full Time",
      "location": "New Jersey",
      "timezone": "EST",
      "department": "Telecom",
      "role": "Developer",
      "manager": "Chandler Bing",
      "skills": [
          "R",
          "Management"
      ]
  },
  "project": null
}];

//skills[0]["engagements"].push({"assignedWeeklyHours":{"03/20/20":20}, startDate:"07/28/2020",endDate:"09/20/2020", employee: {fn:"Bob",ls:"Someone"}});
//skills[1]["engagements"] = [{"assignedWeeklyHours":{"03/20/20":20}, startDate:"07/28/2020",endDate:"09/20/2020", employee: {fn:"Jane",ls:"Doe"}}];