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

function list (limit, token, cb) {
  token = token ? parseInt(token, 10) : 0;
  const connection = getConnection();
  connection.query(
    'SELECT * FROM `user_profile` LIMIT ? OFFSET ?', [limit, token],
    (err, results) => {
      if (err) {
        cb(err);
        return;
      }
      const hasMore = results.length === limit ? token + results.length : false;
      cb(null, results, hasMore);
    }
  );
  connection.end();
}

function create (data, cb) {
  const connection = getConnection();
  connection.query('INSERT INTO `user_profile` SET ?', data, (err, res) => {
    if (err) {
      cb(err);
      return;
    }
    cb(null, res);
  });
  connection.end();
}

function read (id, cb) {
  const connection = getConnection();
  connection.query(
    'SELECT * FROM `user_profile` WHERE `userid` = ?', id, (err, results) => {
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

function update (id, data, cb) {
  const connection = getConnection();
  connection.query(
    'UPDATE `user_profile` SET ? WHERE `userid` = ?', [data, id], (err) => {
      if (err) {
        cb(err);
        return;
      }
      read(id, cb);
    });
  connection.end();
}
function _delete (id, cb) {
  const connection = getConnection();
  connection.query('DELETE FROM `user_profile` WHERE `userid` = ?', id, cb);
  connection.end();
}

module.exports = {
  createSchema: createSchema,
  list: list,
  create: create,
  read: read,
  update: update,
  delete: _delete
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

function createSchema (config) {
  const connection = mysql.createConnection(extend({
    multipleStatements: true
  }, config));

  connection.query(
    `CREATE DATABASE IF NOT EXISTS \`faff\`
      DEFAULT CHARACTER SET = 'utf8'
      DEFAULT COLLATE 'utf8_general_ci';
    USE \`faff\`;
    CREATE TABLE IF NOT EXISTS \`faff\`.\`user_profile\` (
      \`userid\` VARCHAR(255) NOT NULL,
      \`name\` VARCHAR(255) NULL,
      \`address\`TEXT NULL,
      \`email\` VARCHAR(255) NULL,
      \`telephone\` VARCHAR(255) NULL,
      \`gender\` INT NULL,    
      \`favourite_type\` VARCHAR(255) NULL,
      \`age\` INT NULL,
    PRIMARY KEY (\`userid\`));`,
    (err) => {
      if (err) {
        throw err;
      }
      console.log('Successfully created schema');
      connection.end();
    }
  );
}