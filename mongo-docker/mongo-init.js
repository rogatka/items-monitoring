db.createUser(
    {
      user: "uploader",
      pwd: "uploader",
      roles: [
        {
          role: "readWrite",
          db: "items-database"
        }
      ]
    }
);