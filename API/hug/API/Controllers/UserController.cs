using API.Helper;
using Microsoft.AspNetCore.Mvc;
using Models;
using Repository.Interfaces;

// For more information on enabling Web API for empty projects, visit https://go.microsoft.com/fwlink/?LinkID=397860

namespace API.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class UserController : ControllerBase
    {

        private readonly IUnitOfWork unitOfWork;

        public UserController(IUnitOfWork _unitOfWork)
        {
            this.unitOfWork = _unitOfWork;
        }

        // GET: api/<UserController>
        [HttpGet]
        public async Task<IActionResult> GetAll()
        {
            try
            {
                IEnumerable<User> users = await unitOfWork.User.GetAllAsync();

                if (users != null)
                {
                    users = users.Select(x => { 
                                                x.CreatedDate = x.CreatedDate.ToCurrentDateTime(); 
                                                x.ModifiedDate = x.ModifiedDate.ToCurrentDateTime(); 
                                                return x; 
                                        });
                }

                return Ok(users);
            }
            catch (Exception ex)
            {
                return StatusCode(500, "Server Error.");
            }

        }

        // GET api/<UserController>/5
        [HttpGet("{id}")]
        public async Task<IActionResult> Get(int id)
        {
            try
            {
                User user = await unitOfWork.User.GetByIdAsync(id);

                if (user != null)
                {
                    user.CreatedDate = user.CreatedDate.ToCurrentDateTime();
                }

                return Ok(user);
            }
            catch (Exception ex)
            {
                return StatusCode(500, ex.Message);
            }
        }

        // POST api/<UserController>
        [HttpPost]
        public async Task<IActionResult> Post([FromBody] User entity)
        {
            try
            {
                entity.CreatedDate = DateTime.UtcNow;
                entity.Password = Cryptography.Encrypt(entity.Password);
                return Ok(await unitOfWork.User.AddAsync(entity));
            }
            catch (Exception ex)
            {
                return StatusCode(500, ex.Message);
            }
        }

        // PUT api/<UserController>/5
        [HttpPut("{id}")]
        public async Task<IActionResult> Put(int id, [FromBody] User entity)
        {
            try
            {
                return Ok(await unitOfWork.User.UpdateAsync(entity));
            }
            catch (Exception ex)
            {
                return StatusCode(500, ex.Message);
            }
        }

        // DELETE api/<UserController>/5
        [HttpDelete("{id}")]
        public async Task<IActionResult> Delete(int id)
        {
            try
            {
                return Ok(await unitOfWork.User.DeleteAsync(id));
            }
            catch (Exception ex)
            {
                return StatusCode(500, ex.Message);
            }
        }
    }
}
