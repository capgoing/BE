db = db.getSiblingDB('testdb');
db.createUser({
  user: "test",
  pwd: "testPW",
  roles: [{ role: "readWrite", db: "testdb" }]
});
