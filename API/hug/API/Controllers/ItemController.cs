using Microsoft.AspNetCore.Mvc;
using Models;
using Repository.Interfaces;

// For more information on enabling Web API for empty projects, visit https://go.microsoft.com/fwlink/?LinkID=397860

namespace API.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class ItemController : ControllerBase
    {
        private readonly IUnitOfWork repository;

        public ItemController(IUnitOfWork _repository)
        {
            this.repository = _repository;
        }

        // GET: api/<ItemController>
        [HttpGet]
        public async Task<IActionResult> Get(string searchText = null, int locationId = 0)
        {
            return Ok(); // new string[] { "value1", "value2" };
        }

        // GET api/<ItemController>/5
        [HttpGet("{id}")]
        public async Task<IActionResult> Get(int id)
        {
            try
            {
                return Ok(repository.Item.GetByIdAsync(id));
            }
            catch (Exception ex)
            {
                return StatusCode(500, "Server Error.");
            }
        }

        // POST api/<ItemController>
        [HttpPost]
        public async Task<IActionResult> Post([FromBody] Item entity)
        {
            try
            {
                entity.CreatedDate = DateTime.UtcNow;
                return Ok(repository.Item.AddAsync(entity));
            }
            catch (Exception ex)
            {
                return StatusCode(500, "Server Error.");
            }
        }

        // PUT api/<ItemController>/5
        [HttpPut("{id}")]
        public void Put(int id, [FromBody] string value)
        {
        }

        // DELETE api/<ItemController>/5
        [HttpDelete("{id}")]
        public void Delete(int id)
        {
        }
    }
}
