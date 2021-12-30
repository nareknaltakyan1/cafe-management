package com.sflpro.cafe.service;

import com.sflpro.cafe.dto.TableDTO;
import com.sflpro.cafe.entity.Table;
import com.sflpro.cafe.entity.User;
import com.sflpro.cafe.exception.TableNotFoundException;
import com.sflpro.cafe.exception.UserNotFoundException;
import com.sflpro.cafe.repository.TableRepository;
import com.sflpro.cafe.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
public class TableService
{

	private final TableRepository tableRepository;
	private final UserRepository userRepository;

	public TableService(TableRepository tableRepository, UserRepository userRepository)
	{
		this.tableRepository = tableRepository;
		this.userRepository = userRepository;
	}

	@Transactional
	public TableDTO createTable(TableDTO tableDTO)
	{

		Table entity = new Table();
		entity.setChairsAmount(tableDTO.getChairsAmount());

		return tableEntityToDto(tableRepository.save(entity));
	}

	@Transactional
	public TableDTO assignWaiter(Long tableId, Long waiterId)
	{

		Optional<Table> tableOpt = tableRepository.findById(tableId);

		if (!tableOpt.isPresent())
		{
			final String errorMsg = "No table has been found for table id: " + tableId;
			log.error(errorMsg);
			throw new TableNotFoundException();
		}

		Optional<User> userOpt = userRepository.findById(waiterId);

		if (!userOpt.isPresent())
		{
			final String errorMsg = "No waiter has been found for user id: " + waiterId;
			log.error(errorMsg);
			throw new UserNotFoundException();
		}

		Table entity = tableOpt.get();
		entity.setWaiter(userOpt.get());

		tableRepository.save(entity);

		return tableEntityToDto(entity);
	}

	public List<TableDTO> getAllTables()
	{

		return StreamSupport.stream(tableRepository.findAll().spliterator(), false).map(TableService::tableEntityToDto).collect(Collectors.toList());
	}

	public TableDTO getTable(Long tableId)
	{

		Optional<Table> tableOpt = tableRepository.findById(tableId);

		if (!tableOpt.isPresent())
		{
			final String errorMsg = "Table with id doesn't exist: " + tableId;
			log.error(errorMsg);
			throw new TableNotFoundException();
		}

		return tableEntityToDto(tableOpt.get());
	}

	public List<TableDTO> getAssignedTables(Long currentUserId)
	{

		Optional<User> userOpt = userRepository.findById(currentUserId);

		if (!userOpt.isPresent())
		{
			final String errorMsg = "Failed to find logged in user's data";
			log.error(errorMsg);
			throw new IllegalStateException(errorMsg);
		}

		return tableRepository.findByWaiter(userOpt.get()).stream().map(TableService::tableEntityToDto).collect(Collectors.toList());
	}

	public static TableDTO tableEntityToDto(Table entity)
	{

		if (entity == null)
		{
			return null;
		}

		TableDTO tableDTO = new TableDTO();
		tableDTO.setId(entity.getId());
		tableDTO.setWaiter(UserService.userEntityToDto(entity.getWaiter()));
		tableDTO.setChairsAmount(entity.getChairsAmount());

		return tableDTO;
	}
}
