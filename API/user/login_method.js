'use strict';

const extend = require('lodash').assign;
const mysql = require('mysql');
const config = require('../config');


function getConnection () {
  const options = {
    user: config.get('MYSQL_USER'),
    password: config.get('MYSQL_PASSWORD'),
    database: 'faff'
  };

  if (config.get('INSTANCE_CONNECTION_NAME') && config.get('NODE_ENV') === 'production') {
    options.socketPath = `/cloudsql/${config.get('INSTANCE_CONNECTION_NAME')}`;
  }

  return mysql.createConnection(options);
}

function read (id, cb) {
  const connection = getConnection();
  connection.query(
    'SELECT * FROM `user_login` WHERE `username` = ?', id, (err, results) => {
      if (err) {
        cb(err);
        return;
      }
      if (!results.length) {
        cb({
          code: 404,
          message: 'Not found'
        });
        return;
      }
      cb(null, results[0]);
    });
  connection.end();
}
function read_userid (id, cb) {
  const connection = getConnection();
  connection.query(
    'SELECT * FROM `user_login` WHERE `userid` = ?', id, (err, results) => {
      if (err) {
        cb(err);
        return;
      }
      if (!results.length) {
        cb({
          code: 404,
          message: 'Not found'
        });
        return;
      }
      cb(null, results[0]);
    });
  connection.end();
}


function create (data, cb) {
  const connection = getConnection();
  connection.query('INSERT INTO `user_login` SET ?', data, (err, res) => {
    if (err) {
      cb(err);
      return;
    }
    cb(null, res);
  });
  connection.end();
}
module.exports = {
  createSchema: createSchema,
  create: create,
  read: read,
  update: update,
  read_userid : read_userid
};

if (module === require.main) {
  const prompt = require('prompt');
  prompt.start();

  console.log(
    `Running this script directly will allow you to initialize your mysql database.
    This script will not modify any existing tables.`);

  prompt.get(['user', 'password'], (err, result) => {
    if (err) {
      return;
    }
    createSchema(result);
  });
}

function update(userid, data, cb){
  const connection = getConnection();
   connection.query('UPDATE `user_login` SET ? WHERE `userid` = ?' ,[data,userid] , (err, res) => {
    if(err){
      cb(err);
      return;
    }
    cb(null,res);
});
   connection.end();
 }

function createSchema (config) {
  const connection = mysql.createConnection(extend({
    multipleStatements: true
  }, config));

  connection.query(
    `CREATE DATABASE IF NOT EXISTS \`faff\`
      DEFAULT CHARACTER SET = 'utf8'
      DEFAULT COLLATE 'utf8_general_ci';
    USE \`faff\`;
    CREATE TABLE IF NOT EXISTS \`faff\`.\`user_login\` (
      \`username\` VARCHAR(255)  NOT NULL,
       \`userid\` VARCHAR(255) NOT NULL,
      \`password\`VARCHAR(255) NOT NULL,
    PRIMARY KEY (\`username\`));`,
    (err) => {
      if (err) {
        throw err;
      }
      console.log('Successfully created schema');
      connection.end();
    }
  );
}
