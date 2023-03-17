db.createUser(
    {
      user: "uploader",
      pwd: "uploader",
      roles: [
        {
          role: "readWrite",
          db: "test"
        }
      ]
    }
);