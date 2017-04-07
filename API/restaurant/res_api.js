'use strict'

const express = require('express');
const express = require('express');
const bodyParser = require('body-parser');
const config = require('../config');
const router = express.Router();

router.use(bodyParser.json());
router.use(bodyParser.urlencoded({
    extended: true
}));

function getModel () {
  return require(`./login_method`);
}

router.get('/:resid', (req, res, next) => {
  getModel().read(req.params.username, (err, entity) => {
    if (err) {
      next(err);
      return;
    }
    res.json(entity);
  });
});

router.get('/',(req,res,next)=>{
	getModel().

})