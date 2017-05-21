'use strict';

var table_name = "promotion_list"
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
    'SELECT * FROM `'+table_name+'` LIMIT ? OFFSET ?', [limit, token],
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

function get_count (cb) {
  const connection = getConnection();
  connection.query(
    'SELECT count(promotionid) as n FROM `promotion_list` ', (err, results) => {
      if (err) {
        cb(err);
        return;
      }
      cb(null, results);
    }
  );
  connection.end();
}

function create (data, cb) {
  const connection = getConnection();
  connection.query('INSERT INTO `'+table_name+'` SET ?', data, (err, res) => {
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
    'SELECT * FROM `'+table_name+'` WHERE `promotionid` = ?', id, (err, results) => {
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
    'UPDATE `'+table_name+'` SET ? WHERE `promotionid` = ?', [data, id], (err) => {
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
  connection.query('DELETE FROM `'+table_name+'` WHERE `promotionid` = ?', id, cb);
  connection.end();
}

module.exports = {
  get_count: get_count,
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
    CREATE TABLE IF NOT EXISTS \`faff\`.\``+table_name+`\`(
      \`promotionid\` INT UNSIGNED NOT NULL AUTO_INCREMENT,
      \`promotionname\` VARCHAR(255) NULL,
	  \`promotionpictureurl\` TEXT NULL,
      \`promotionstartdate\`TEXT NULL,
      \`promotionenddate\` VARCHAR(255) NULL,
      \`promotiondetail\` VARCHAR(255) NULL,
      \`promotionlocation\` VARCHAR(255) NULL,
       \`promotionuserid\` VARCHAR(255) NULL,
    PRIMARY KEY (\`promotionid\`));`,
    (err) => {
      if (err) {
        throw err;
      }
      console.log('Successfully created schema');
      connection.end();
    }
  );
}