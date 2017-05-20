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
/*select * from promotion_list pro inner join restaurant_promotion res on pro.promotionid = res.promotionid where resid= test0914121493662297; 
update restaurant_promotion set promotionid = 8 where promotionid = 1;*/

function all_list (cb) {
  const connection = getConnection();
  connection.query(
    'SELECT * FROM `restaurant_promotion` ', (err, results) => {
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
  connection.query('INSERT INTO `restaurant_promotion` SET ?', data, (err, res) => {
    if (err) {
      cb(err);
      return;
    }
     cb(null, res);
  });
  connection.end();
}

/*function read (id, cb) {
  const connection = getConnection();
  connection.query(
    'SELECT * FROM `restaurant_promotion` WHERE `resid` = ?', id, (err, results) => {
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
*/
function read (id, cb) {
  const connection = getConnection();
  connection.query(
    'SELECT * from promotion_list pro INNER JOIN restaurant_promotion res on pro.promotionid = res.promotionid where resid = ? ', id, (err, results) => {
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
      cb(null, results);
    });
  connection.end();
}

function update (id, data, cb) {
  const connection = getConnection();
  connection.query(
    'UPDATE `restaurant_promotion` SET ? WHERE `resid` = ?', [data, id], (err, results) => {
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
  connection.query('DELETE restaurant_promotion,promotion_list FROM restaurant_promotion INNER JOIN promotion_list WHERE promotion_list.promotionid = restaurant_promotion.promotionid AND restaurant_promotion.promotionid = ?' , id, cb);
  connection.end();
}

function update_score(data, id, cb){
  const connection = getConnection();
  connection.query('UPDATE `restaurant_promotion` SET  ? WHERE `resid` = ? ',[ data , id] , (err, results) =>{
    if(err){
      cb(err);
      return;
    }
    cb(null, results);
  });
 connection.end();
}

module.exports = {
  all_list: all_list,
  create: create,
  createSchema: createSchema,
  read: read,
  update: update,
  delete: _delete,
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
    CREATE TABLE IF NOT EXISTS \`faff\`.\`restaurant_promotion\` (
       \`promotionid\` VARCHAR(255) NOT NULL,
       \`resid\` VARCHAR(255) NOT NULL,
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