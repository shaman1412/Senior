'use strict'

const express = require('express');
const bodyParser = require('body-parser');
const config = require('../config');
const router = express.Router();

router.use(bodyParser.json());
router.use(bodyParser.urlencoded({
    extended: true
}));

function getModel () {
  return require('./restaurant_promotion_method');
}

router.get('/', (req, res, next) => {
  getModel().all_list((err, entities, cursor) => {
    if (err) {
      next(err);
      return;
    }
    res.json({
      items: entities,
    });
  });
});

router.get('/resid/:resid', (req, res, next) => {
  getModel().read(req.params.resid, (err, entity) => {
    if (err) {
      next(err);
      return;
    }
    res.json(entity);
  });
});



router.get('/promotionid/:promotionid', (req, res, next) => {
  getModel().read(req.params.promotionid, (err, entity) => {
    if (err) {
      next(err);
      return;
    }
    res.json(entity);
  });
});

router.post('/create', (req,res,next) => {
  const json = req.body;
  
  const data  =  {
    resid : json.resid,
	promotionid : json.promotionid
  }

  getModel().create(data, (err, entity) => {
    if(err){
      next(err);
      return;
    }
    res.json(entity);
  });
  //return res.send(data.picture);
});

/*router.delete('/resid/del/:resid', (req, res, next) => {
  getModel().delete(req.params.resid , (err, entity) =>{
    if(err){
      next(err);
      return;
    }
    res.json(entity);
  })
})
*/
router.delete('/promotionid/del/:promotionid', (req, res, next) => {
  getModel().delete(req.params.promotionid , (err, entity) =>{
    if(err){
      next(err);
      return;
    }
    res.json(entity);
  })
})

module.exports = router; 