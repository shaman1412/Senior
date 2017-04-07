'use strict'

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

function all_list (limit, token, cb) {
  const connection = getConnection();
  connection.query(
    'SELECT * FROM `user` ', (err, results) => {
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

function filter_list (type, cb) {
  const connection = getConnection();
const type_split = type.split(",");
const sql = 'SELECT * FROM `user` WHERE  type_food =';
for(let i = 0; i< type_split.length; i++){
	let 
}
  connection.query(
    'SELECT * FROM `user` WHERE  type_food = ? ', (err, results) => {
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
  connection.query('INSERT INTO `user` SET ?', data, (err, res) => {
    if (err) {
      cb(err);
      return;
    }
    read(res.insertId, cb);
  });
  connection.end();
}

function read (id, cb) {
  const connection = getConnection();
  connection.query(
    'SELECT * FROM `user` WHERE `userid` = ?', id, (err, results) => {
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
    'UPDATE `user` SET ? WHERE `userid` = ?', [data, id], (err) => {
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
  connection.query('DELETE FROM `user` WHERE `userid` = ?', id, cb);
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
    CREATE TABLE IF NOT EXISTS \`faff\`.\`restaurant_profile\` (
      \`id\` INT UNSIGNED NOT NULL AUTO_INCREMENT,
       \`restaurantid\` VARCHAR(255) NULL,
      \`name\` VARCHAR(255) NULL,
       \`type_food\` VARCHAR(255) NULL,
       \`description\` VARCHAR(255) NULL,
       \`period\` VARCHAR(255) NULL,
      \`address\` VARCHAR(255) NULL,
      \`location\` VARCHAR(255) NULL,
      \`score\` Float NULL,
      \`create_time\` DATETIME NULL,
    PRIMARY KEY (\`id\`));`,
    (err) => {
      if (err) {
        throw err;
      }
      console.log('Successfully created schema');
      connection.end();
    }
  );
}