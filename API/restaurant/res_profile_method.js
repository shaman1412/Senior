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

function all_list ( cb) {
  const connection = getConnection();
  connection.query(
    'SELECT * FROM `restaurant_profile` ', (err, results) => {
      if (err) {
        cb(err);
        return;
      }
      //const hasMore = results.length === limit ? token + results.length : false;
      cb(null, results);
    }
  );
  connection.end();
}



function create (data, cb) {
  const connection = getConnection();
  connection.query('INSERT INTO `restaurant_profile` SET ?', data, (err, res) => {
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
    'SELECT * FROM `restaurant_profile` WHERE `res_id` = ?', id, (err, results) => {
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
    'UPDATE `restaurant_profile` SET ? WHERE `res_id` = ?', [data, id], (err, results) => {
      if (err) {
        cb(err);
        return;
      }
      cb(null, results);
    });
  connection.end();
}
function _delete (id, cb) {
  const connection = getConnection();
  connection.query('DELETE FROM `restaurant_profile` WHERE `res_id` = ?', id, cb);
  connection.end();
}

function update_score(data, id, cb){
  const connection = getConnection();
  connection.query('UPDATE `restaurant_profile` SET  ? WHERE `res_id` = ? ',[ data , id] , (err, results) =>{
    if(err){
      cb(err);
      return;
    }
    cb(null, results);
  });
 connection.end();
}

function calculate_score(id, data , cb){
  const connection = getConnection();
  connection.query('SELECT vote,score FROM  `restaurant_profile` WHERE `res_id` = ? ' , id , (err , results) =>{
    if(err){
      cb(err);
      return;
    }

     const  vote = results[0].vote + 1;
     const result = Math.round( ( results[0].score + parseInt(data, 10) ) / vote ) ;
     const getdata  = {
      vote : vote,
      score : result
     }
    update_score(getdata , id , (err, entity) => {
      if(err){
      cb(err);
      return;
    }
     cb(null, entity);
    })
  })
  connection.end();
}

module.exports = {
  create: create,
  createSchema: createSchema,
  read: read,
  update: update,
  delete: _delete,
  calculate_score : calculate_score
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
       \`res_id\` VARCHAR(255) NOT NULL,
      \`name\` VARCHAR(255) NULL,
       \`type_food\` VARCHAR(255) NULL,
       \`description\` VARCHAR(255) NULL,
       \`period\` VARCHAR(255) NULL,
      \`address\` VARCHAR(255) NULL,
      \`location\` VARCHAR(255) NULL,
      \`score\` Float NULL,
        \`vote\` Float NULL,
      \`create_time\` TIMESTAMP NULL,
    PRIMARY KEY (\`res_id\`));`,
    (err) => {
      if (err) {
        throw err;
      }
      console.log('Successfully created schema');
      connection.end();
    }
  );
}